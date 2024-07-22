package org.example.endpointmonitoring.services

import jakarta.persistence.EntityNotFoundException
import org.example.endpointmonitoring.models.*
import org.example.endpointmonitoring.repositories.MonitoredEndpointRepository
import org.example.endpointmonitoring.repositories.MonitoringResultRepository
import org.example.endpointmonitoring.utils.MonitoredEndpointInDTOValidator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

interface MonitoredEndpointService {
    fun getAllEndpoints(resultLimit: Int?): List<MonitoredEndpointOutDTO>
    fun getEndpoint(monitoredEndpointId: UUID, resultLimit: Int?): MonitoredEndpointOutDTO
    fun createEndpoint(monitoredEndpointInDTO: MonitoredEndpointInDTO): MonitoredEndpointOutDTO
    fun updateEndpoint(
        monitoredEndpointInDTO: MonitoredEndpointInDTO,
        monitoredEndpointId: UUID
    ): MonitoredEndpointOutDTO

    fun deleteEndpoint(monitoredEndpointId: UUID): MonitoredEndpointOutDTO
}

@Service
class MonitoredEndpointServiceImpl(
    private val endpointRepository: MonitoredEndpointRepository,
    private val resultRepository: MonitoringResultRepository,
    private val monitoringService: MonitoringService
) : MonitoredEndpointService {
    private lateinit var authenticatedUserId: UUID

    override fun getAllEndpoints(resultLimit: Int?): List<MonitoredEndpointOutDTO> {
        authenticatedUserId = getAuthenticatedUserId()
        return endpointRepository.findAllByOwnerId(authenticatedUserId).asSequence()
            .map {
                MonitoredEndpointOutDTO(
                    it.id,
                    it.name,
                    it.url,
                    it.createdAt,
                    it.lastCheck,
                    it.monitoringInterval,
                    getResultOutDTOList(it.id, resultLimit),
                    it.ownerId
                )
            }
            .toList()
    }

    override fun getEndpoint(monitoredEndpointId: UUID, resultLimit: Int?): MonitoredEndpointOutDTO {
        val endpoint: MonitoredEndpoint = try {
            this.endpointRepository.findById(monitoredEndpointId)
        } catch (e: Exception) {
            throw EntityNotFoundException("Could not find endpoint: $monitoredEndpointId", e)
        }
        checkIfAuthenticatedUserIsAllowedForEndpoint(endpoint)
        return MonitoredEndpointOutDTO(
            endpoint.id,
            endpoint.name,
            endpoint.url,
            endpoint.createdAt,
            endpoint.lastCheck,
            endpoint.monitoringInterval,
            getResultOutDTOList(monitoredEndpointId, resultLimit),
            endpoint.ownerId
        )

    }

    override fun createEndpoint(monitoredEndpointInDTO: MonitoredEndpointInDTO): MonitoredEndpointOutDTO {
        MonitoredEndpointInDTOValidator.validate(monitoredEndpointInDTO)
        authenticatedUserId = getAuthenticatedUserId()

        val newEndpoint = MonitoredEndpoint()
        newEndpoint.name = monitoredEndpointInDTO.name
        newEndpoint.url = monitoredEndpointInDTO.url
        newEndpoint.monitoringInterval = monitoredEndpointInDTO.monitoringInterval
        newEndpoint.ownerId = authenticatedUserId

        return processMonitoredEndpoint(newEndpoint)
    }

    override fun updateEndpoint(
        monitoredEndpointInDTO: MonitoredEndpointInDTO,
        monitoredEndpointId: UUID
    ): MonitoredEndpointOutDTO {
        MonitoredEndpointInDTOValidator.validate(monitoredEndpointInDTO, false)

        val endpointToUpdate = try {
            endpointRepository.findById(monitoredEndpointId)
        } catch (e: Exception) {
            throw EntityNotFoundException("Could not find endpoint: $monitoredEndpointId", e)
        }
        checkIfAuthenticatedUserIsAllowedForEndpoint(endpointToUpdate)

        endpointToUpdate.name = monitoredEndpointInDTO.name
        endpointToUpdate.monitoringInterval = monitoredEndpointInDTO.monitoringInterval

        return processMonitoredEndpoint(endpointToUpdate)
    }

    override fun deleteEndpoint(monitoredEndpointId: UUID): MonitoredEndpointOutDTO {
        val endpointToDelete = try {
            endpointRepository.findById(monitoredEndpointId)
        } catch (e: Exception) {
            throw EntityNotFoundException("Could not find endpoint: $monitoredEndpointId", e)
        }
        checkIfAuthenticatedUserIsAllowedForEndpoint(endpointToDelete)
        endpointRepository.delete(endpointToDelete)

        monitoringService.removeMonitoringTask(endpointToDelete)

        return MonitoredEndpointOutDTO(
            endpointToDelete.id,
            endpointToDelete.name,
            endpointToDelete.url,
            endpointToDelete.createdAt,
            endpointToDelete.lastCheck,
            endpointToDelete.monitoringInterval,
            emptyList(),
            endpointToDelete.ownerId
        )
    }

    fun getResultOutDTOList(endpointId: UUID, resultLimit: Int?): List<MonitoringResultOutDTO> {
        val allResults = resultRepository.findAllByMonitoredEndpointIdOrderByCheckedAtDesc(endpointId)

        return allResults.asSequence()
            .map { it.toDto() }
            .take(if (resultLimit == null) allResults.size else if (resultLimit < 0) 0 else resultLimit)
            .toList()
    }

    private fun processMonitoredEndpoint(endpoint: MonitoredEndpoint): MonitoredEndpointOutDTO {
        val updatedEndpoint: MonitoredEndpoint = endpointRepository.save(endpoint)

        val createdEndpointOutDTO = MonitoredEndpointOutDTO(
            updatedEndpoint.id,
            updatedEndpoint.name,
            updatedEndpoint.url,
            updatedEndpoint.createdAt,
            updatedEndpoint.lastCheck,
            updatedEndpoint.monitoringInterval,
            emptyList(),
            updatedEndpoint.ownerId
        )

        monitoringService.updateMonitoringTask(updatedEndpoint)
        return createdEndpointOutDTO
    }

    private fun getAuthenticatedUserId(): UUID {
        val principal: Any? = SecurityContextHolder.getContext().authentication.principal
        return if (principal is AppUser) principal.id else throw Exception("User not authenticated")
    }

    private fun checkIfAuthenticatedUserIsAllowedForEndpoint(endpoint: MonitoredEndpoint) {
        authenticatedUserId = getAuthenticatedUserId()
        if (authenticatedUserId != endpoint.ownerId) {
            throw Exception("User is not allowed to access endpoint")
        }
    }
}

fun MonitoringResult.toDto(): MonitoringResultOutDTO = MonitoringResultOutDTO(
    this.id, this.checkedAt, this.httpCode, this.payload
)
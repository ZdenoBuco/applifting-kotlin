package org.example.endpointmonitoring.controllers

import org.example.endpointmonitoring.models.MonitoredEndpointInDTO
import org.example.endpointmonitoring.models.MonitoredEndpointOutDTO
import org.example.endpointmonitoring.services.MonitoredEndpointServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/monitoredEndpoints")
class MonitoredEndpointController(private val monitoredEndpointService: MonitoredEndpointServiceImpl) {

    @GetMapping("/{monitoredEndpointId}")
    fun getEndpoint(@PathVariable monitoredEndpointId: UUID, @RequestParam(required = false) resultLimit: Int?): ResponseEntity<MonitoredEndpointOutDTO> {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(monitoredEndpointService.getEndpoint(monitoredEndpointId, resultLimit))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error", ex)
        }
    }

    @GetMapping
    fun getEndpoints(@RequestParam(required = false) resultLimit: Int?): ResponseEntity<List<MonitoredEndpointOutDTO>> {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(monitoredEndpointService.getAllEndpoints(resultLimit))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error", ex)
        }
    }

    @PostMapping
    fun createMonitoredEndpoint(@RequestBody newEndpoint: MonitoredEndpointInDTO): ResponseEntity<MonitoredEndpointOutDTO> {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(monitoredEndpointService.createEndpoint(newEndpoint))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error", ex)
        }
    }

    @PutMapping("/{monitoredEndpointId}")
    fun updateMonitoredEndpoint(@PathVariable monitoredEndpointId: UUID, @RequestBody newEndpoint: MonitoredEndpointInDTO): ResponseEntity<MonitoredEndpointOutDTO> {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(monitoredEndpointService.updateEndpoint(newEndpoint, monitoredEndpointId))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error", ex)
        }
    }

    @DeleteMapping("/{monitoredEndpointId}")
    fun deleteMonitoredEndpoint(@PathVariable monitoredEndpointId: UUID): ResponseEntity<MonitoredEndpointOutDTO> {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(monitoredEndpointService.deleteEndpoint(monitoredEndpointId))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error", ex)
        }
    }
}
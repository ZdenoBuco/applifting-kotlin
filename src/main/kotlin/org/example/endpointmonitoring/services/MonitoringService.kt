package org.example.endpointmonitoring.services

import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.example.endpointmonitoring.models.MonitoredEndpoint
import org.example.endpointmonitoring.models.MonitoringResult
import org.example.endpointmonitoring.repositories.MonitoredEndpointRepository
import org.example.endpointmonitoring.repositories.MonitoringResultRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


interface MonitoringService {
    @PostConstruct
    fun init()

    @Transactional
    fun updateMonitoringTask(updatedEndpoint: MonitoredEndpoint)

    @Transactional
    fun removeMonitoringTask(deletedEndpoint: MonitoredEndpoint)
}

@Service
class MonitoringServiceImpl(
    private val endpointRepository: MonitoredEndpointRepository,
    private val resultRepository: MonitoringResultRepository
) : MonitoringService {
    private val executorService = Executors.newScheduledThreadPool(10)
    private val tasks: MutableMap<UUID, ScheduledFuture<*>> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(MonitoringServiceImpl::class.java)

    @PostConstruct
    override fun init() {
        val endpoints = endpointRepository.findAll()
        for (endpoint in endpoints) scheduleMonitoringTask(endpoint)
    }

    private fun scheduleMonitoringTask(endpoint: MonitoredEndpoint) {
        val task = Runnable { checkEndpoint(endpoint) }
        val future = executorService.scheduleAtFixedRate(
            { task.run() },
            0,
            endpoint.monitoringInterval.toLong(),
            TimeUnit.SECONDS
        )
        tasks[endpoint.id] = future
    }

    private fun checkEndpoint(endpoint: MonitoredEndpoint) {
        try {
            val restTemplate = RestTemplate()
            val response = restTemplate.getForEntity(endpoint.url, String::class.java)
            createMonitoringResult(endpoint, response.statusCode.value(), response.body)
            endpoint.lastCheck = LocalDateTime.now()
            endpointRepository.save(endpoint)
            logger.info("Monitored endpoint ${endpoint.id} has been checked")
        } catch (e: Exception) {
            createMonitoringResult(endpoint, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
            endpoint.lastCheck = LocalDateTime.now()
            endpointRepository.save(endpoint)
            logger.warn("Error occured during checking endpoint ${endpoint.id}.")
        }
    }

    private fun createMonitoringResult(endpoint: MonitoredEndpoint, statusCode: Int, body: String?) {
        val monitoringResult = MonitoringResult()
        monitoringResult.monitoredEndpointId = endpoint.id
        monitoringResult.checkedAt = endpoint.lastCheck
        monitoringResult.httpCode = statusCode
        monitoringResult.payload = body ?: ""
        resultRepository.save(monitoringResult)
    }

    override fun updateMonitoringTask(updatedEndpoint: MonitoredEndpoint) {
        val future = tasks.remove(updatedEndpoint.id)
        future?.cancel(true)
        scheduleMonitoringTask(updatedEndpoint)
    }

    override fun removeMonitoringTask(deletedEndpoint: MonitoredEndpoint) {
        val future = tasks.remove(deletedEndpoint.id) ?: return
        future.cancel(true)
    }
}

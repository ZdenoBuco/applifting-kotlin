package org.example.endpointmonitoring.repositories

import org.example.endpointmonitoring.models.MonitoredEndpoint
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MonitoredEndpointRepository : JpaRepository<MonitoredEndpoint, Long> {
    fun findAllByOwnerId(appUserId: UUID): List<MonitoredEndpoint>
    fun findById(endpointId: UUID): MonitoredEndpoint
}
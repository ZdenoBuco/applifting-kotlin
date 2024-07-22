package org.example.endpointmonitoring.repositories

import org.example.endpointmonitoring.models.MonitoringResult
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MonitoringResultRepository : JpaRepository<MonitoringResult, Long> {
    fun findAllByMonitoredEndpointIdOrderByCheckedAtDesc(appUserId: UUID?): List<MonitoringResult>

//    fun findSomething(appUserId: UUID?): List<MonitoringResult> =
//        findAllByMonitoredEndpointIdOrderByCheckedAtDesc(appUserId)
}
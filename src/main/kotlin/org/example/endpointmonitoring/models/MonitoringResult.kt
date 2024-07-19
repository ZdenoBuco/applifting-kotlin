package org.example.endpointmonitoring.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity
class MonitoringResult() {
    @Id
    @GeneratedValue
    var id: UUID = UUID.randomUUID()
    var checkedAt: LocalDateTime = LocalDateTime.now()
    var httpCode: Int = 200

    @Column(columnDefinition = "TEXT")
    var payload: String = ""
    var monitoredEndpointId: UUID = UUID.randomUUID()
}

data class MonitoringResultOutDTO(
    private var id: UUID,
    private val checkedAt: LocalDateTime,
    private val httpCode: Int,
    private val payload: String
)
package org.example.endpointmonitoring.models

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.*

@Entity
class MonitoredEndpoint() {
    @Id
    @GeneratedValue
    var id: UUID = UUID.randomUUID()
    var name: String = ""
    var url: String = ""
    var createdAt: LocalDateTime = LocalDateTime.now()
    var lastCheck: LocalDateTime = createdAt
    var monitoringInterval: Int = 1
    @OneToMany(mappedBy = "monitoredEndpointId", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var monitoringResults: List<MonitoringResult> = emptyList()
    var ownerId: UUID = UUID.randomUUID()
}

data class MonitoredEndpointInDTO(
    val name: String,
    val url: String,
    @Size(min = 5, max = 10000) // swagger automaticky podla anotacii updatuje popis daneho parametru v scheme
    val monitoringInterval: Int
)

data class MonitoredEndpointOutDTO(
    val id: UUID,
    val name: String,
    val url: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val lastCheck: LocalDateTime,
    val monitoringInterval: Int,
    val monitoringResults: List<MonitoringResultOutDTO>,
    val ownerId: UUID
)

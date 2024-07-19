package org.example.endpointmonitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EndpointMonitoringApplication

fun main(args: Array<String>) {
    runApplication<EndpointMonitoringApplication>(*args)
}

package org.example.endpointmonitoring.utils

import org.example.endpointmonitoring.models.MonitoredEndpointInDTO

private const val NAME_MAX_LENGTH = 255
private const val URL_MAX_LENGTH = 255
private const val MONITORING_INTERVAL_MIN = 5
private const val MONITORING_INTERVAL_MAX = 100000

class MonitoredEndpointInDTOValidator {
    companion object {
        fun validate(
            monitoredEndpointInDTO: MonitoredEndpointInDTO,
            includeUrlValidation: Boolean = true
        ) {
            if (monitoredEndpointInDTO.name.length > NAME_MAX_LENGTH) {
                throw Exception("Endpoint name must be up to $NAME_MAX_LENGTH characters long")
            }
            if (monitoredEndpointInDTO.monitoringInterval > MONITORING_INTERVAL_MAX || monitoredEndpointInDTO.monitoringInterval < MONITORING_INTERVAL_MIN) {
                throw Exception("Endpoint monitoring interval must be between $MONITORING_INTERVAL_MIN and $MONITORING_INTERVAL_MAX")
            }
            if (includeUrlValidation && monitoredEndpointInDTO.url.length > URL_MAX_LENGTH) {
                throw Exception("Endpoint URL must be up to $URL_MAX_LENGTH characters long")
            }
        }
    }
}
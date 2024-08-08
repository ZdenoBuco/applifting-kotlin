package org.example.endpointmonitoring.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.example.endpointmonitoring.exceptions.SpecialException
import org.example.endpointmonitoring.models.MonitoredEndpointInDTO
import org.example.endpointmonitoring.models.MonitoredEndpointOutDTO
import org.example.endpointmonitoring.services.MonitoredEndpointService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/monitoredEndpoints")
class MonitoredEndpointController(private val monitoredEndpointService: MonitoredEndpointService) {
    //    custom definicie odpovedi:
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found the endpoint",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = MonitoredEndpointOutDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid id supplied...bla bla bla",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404", description = "Endpoint not found...bla bla bla",
                content = [Content()]
            )
        ]
    )
    @Operation(summary = "Get single endpoint") // kratky popis endpointu
    @GetMapping("/{monitoredEndpointId}")
    @ResponseStatus(HttpStatus.OK)
    fun getEndpoint(
        @Parameter(description = "Id of the desired endpoint") @PathVariable monitoredEndpointId: UUID,
//            swagger - popis parametru
        @Parameter(
            name = "resultLimit",
            description = "Number of results for the endpoint. If null, all results will be retrieved",
            example = "10",
            required = false
        ) @RequestParam(required = false) resultLimit: Int?
    ): MonitoredEndpointOutDTO = monitoredEndpointService.getEndpoint(monitoredEndpointId, resultLimit)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getEndpoints(@RequestParam(required = false) resultLimit: Int?): List<MonitoredEndpointOutDTO> =
        monitoredEndpointService.getAllEndpoints(resultLimit)


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMonitoredEndpoint(@RequestBody newEndpoint: MonitoredEndpointInDTO): MonitoredEndpointOutDTO =
        monitoredEndpointService.createEndpoint(newEndpoint)

    @PutMapping("/{monitoredEndpointId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateMonitoredEndpoint(
        @PathVariable monitoredEndpointId: UUID, @RequestBody newEndpoint: MonitoredEndpointInDTO
    ): MonitoredEndpointOutDTO = monitoredEndpointService.updateEndpoint(newEndpoint, monitoredEndpointId)

    @DeleteMapping("/{monitoredEndpointId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteMonitoredEndpoint(@PathVariable monitoredEndpointId: UUID): MonitoredEndpointOutDTO =
        monitoredEndpointService.deleteEndpoint(monitoredEndpointId)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> = ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(SpecialException::class)
    fun handleSpecialException(e: SpecialException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.I_AM_A_TEAPOT)
}


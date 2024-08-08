package org.example.endpointmonitoring.controllers
//
//import org.example.endpointmonitoring.exceptions.SpecialException
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.ResponseStatus
//import org.springframework.web.bind.annotation.RestControllerAdvice
//
//// vo swagger automaticky hodi dokumentaciu na tieto globalne exceptions...plati pre vsetky endpoint....da sa specifikovat ib akonkretny package.
//// pokial chcem spravit dokumentaci na jednotlive controllers, tak musim pouzit @ApiResponses
//@RestControllerAdvice()
//class MonitoredEndpointControllerExceptionHandler {
//    @ExceptionHandler(Exception::class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    fun handleException(ex: Exception): ResponseEntity<String> {
//        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
//    }
//    @ExceptionHandler(SpecialException::class)
//    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
//    fun handleSpecialException(ex: RuntimeException): ResponseEntity<String> {
//        return ResponseEntity(ex.message, HttpStatus.I_AM_A_TEAPOT)
//    }
//}
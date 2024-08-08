package org.example.endpointmonitoring.controllers

import org.example.endpointmonitoring.models.AppUserRegistrationInDTO
import org.example.endpointmonitoring.models.AppUserRegistrationOutDTO
import org.example.endpointmonitoring.services.AppUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val appUserService: AppUserService) {

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUpUser(
        @RequestBody appUserRegistrationDTO: AppUserRegistrationInDTO
    ): AppUserRegistrationOutDTO =
        appUserService.createUser(appUserRegistrationDTO)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> = ResponseEntity(e.message, HttpStatus.NOT_FOUND)
}
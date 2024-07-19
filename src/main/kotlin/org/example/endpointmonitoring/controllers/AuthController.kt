package org.example.endpointmonitoring.controllers

import org.example.endpointmonitoring.models.AppUserRegistrationInDTO
import org.example.endpointmonitoring.models.AppUserRegistrationOutDTO
import org.example.endpointmonitoring.services.AppUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val appUserService: AppUserService) {

    @PostMapping("register")
    fun signUpUser(@RequestBody appUserRegistrationDTO: AppUserRegistrationInDTO): ResponseEntity<AppUserRegistrationOutDTO> {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.createUser(appUserRegistrationDTO))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error", ex)
        }
    }
}
package org.example.endpointmonitoring.services

import org.example.endpointmonitoring.models.AppUser
import org.example.endpointmonitoring.models.AppUserRegistrationInDTO
import org.example.endpointmonitoring.models.AppUserRegistrationOutDTO
import org.example.endpointmonitoring.repositories.AppUserRepository
import org.example.endpointmonitoring.utils.AppUserRegistrationInDTOValidator
import org.springframework.stereotype.Service
import java.util.*

interface AppUserService {
    fun createUser(appUserRegistrationInDTO: AppUserRegistrationInDTO): AppUserRegistrationOutDTO
}

@Service
class AppUserServiceImpl(private val appUserRepository: AppUserRepository) : AppUserService {
    override fun createUser(appUserRegistrationInDTO: AppUserRegistrationInDTO): AppUserRegistrationOutDTO {
        AppUserRegistrationInDTOValidator.validate(appUserRegistrationInDTO)


        if (appUserRepository.existsAppUserByEmail(appUserRegistrationInDTO.email)) {
            throw Exception("A user with this email already exists. Please choose a different email.")
        }
        val accessToken = UUID.randomUUID().toString()

        val appUser = AppUser()
        appUser.userName = appUserRegistrationInDTO.username
        appUser.email = appUserRegistrationInDTO.email
        appUser.accessToken = accessToken
        appUser.monitoredEndpoints = emptyList()

        appUserRepository.save(appUser)
        return AppUserRegistrationOutDTO(accessToken)
    }
}

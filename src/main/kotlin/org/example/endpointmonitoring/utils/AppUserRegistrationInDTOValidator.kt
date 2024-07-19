package org.example.endpointmonitoring.utils

import org.example.endpointmonitoring.models.AppUserRegistrationInDTO
import java.util.regex.Pattern

private const val USERNAME_MAX_LENGTH: Int = 255
private const val EMAIL_MAX_LENGTH: Int = 255
private const val EMAIL_REGEX: String =
    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
private val pattern: Pattern = Pattern.compile(EMAIL_REGEX)

class AppUserRegistrationInDTOValidator {
    companion object {
        fun validate(userRegistrationInDTO: AppUserRegistrationInDTO) {
            if (userRegistrationInDTO.username.length > USERNAME_MAX_LENGTH) {
                throw Exception("Username must be up to $USERNAME_MAX_LENGTH characters long")
            }
            if (userRegistrationInDTO.email.length > EMAIL_MAX_LENGTH) {
                throw Exception("Email must be up to $EMAIL_MAX_LENGTH characters long")
            }
            if (!pattern.matcher(userRegistrationInDTO.email).matches()) {
                throw Exception("Invalid email address")
            }
        }
    }
}

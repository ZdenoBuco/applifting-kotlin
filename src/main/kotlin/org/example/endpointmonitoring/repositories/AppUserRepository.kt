package org.example.endpointmonitoring.repositories

import org.example.endpointmonitoring.models.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AppUserRepository : JpaRepository<AppUser, UUID?> {
    fun existsAppUserByEmail(email: String): Boolean

    fun findAppUserByAccessToken(accessToken: String): AppUser?

    fun findAppUserByEmail(accessToken: String): AppUser?
}
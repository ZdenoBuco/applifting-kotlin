package org.example.endpointmonitoring.models

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
class AppUser() : UserDetails {
    @Id
    @GeneratedValue
    var id: UUID = UUID.randomUUID()
    var userName: String = ""

    @Column(unique = true)
    var email: String = ""

    @Column(unique = true)
    var accessToken: String = ""

    @OneToMany(mappedBy = "ownerId", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var monitoredEndpoints: List<MonitoredEndpoint> = emptyList()

    override fun getAuthorities(): MutableCollection<GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return accessToken
    }

    override fun getUsername(): String {
        return email
    }
}

data class AppUserRegistrationInDTO(
    val username: String,
    val email: String
)

data class AppUserRegistrationOutDTO(
    val accessToken: String
)
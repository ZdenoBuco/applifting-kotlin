package org.example.endpointmonitoring.security

import jakarta.servlet.DispatcherType
import org.example.endpointmonitoring.repositories.AppUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val appUserRepository: AppUserRepository,
) {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService {
            appUserRepository.findAppUserByEmail(it) ?: throw UsernameNotFoundException("User not found")
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Configuration
    @EnableWebSecurity
    class SecurityFilterChainConfig(
        private val tokenAuthFilter: TokenAuthenticationFilter,
        private val authProvider: AuthenticationProvider
    ) {
        @Bean
        fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
            http
                .csrf { it.disable() }
                .authorizeHttpRequests { auth ->
                    auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                }
                .sessionManagement { sm ->
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
                .authenticationProvider(authProvider)
                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

            return http.build()
        }
    }
}
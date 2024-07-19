package org.example.endpointmonitoring.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.endpointmonitoring.models.AppUser
import org.example.endpointmonitoring.repositories.AppUserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthenticationFilter(private val appUserRepository: AppUserRepository) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = request.getHeader("accessToken")
        if (accessToken != null) {
            val appUser: AppUser? = appUserRepository.findAppUserByAccessToken(accessToken)
            if (appUser != null) {
                val authentication = PreAuthenticatedAuthenticationToken(appUser, null)
                SecurityContextHolder.getContext().authentication = authentication
                SecurityContextHolder.getContext().authentication.isAuthenticated = true
            }
        }
        filterChain.doFilter(request, response)
    }
}

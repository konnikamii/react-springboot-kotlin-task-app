package springboot.kotlin.task.app.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import springboot.kotlin.task.app.congif.JwtProperties
import springboot.kotlin.task.app.dto.AuthIn
import springboot.kotlin.task.app.dto.AuthOut
import java.util.*

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val jwtService: JwtService,
    private val jwtProperties: JwtProperties
) {
    fun authenticate(authIn: AuthIn): AuthOut {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authIn.username,
                authIn.password
            )
        )
        val user = userDetailsService.loadUserByUsername(authIn.username)
        val accessToken = jwtService.generate(
            user,
            Date(System.currentTimeMillis() + jwtProperties.expiration)
        )

        return AuthOut(accessToken)
    }
}
package springboot.kotlin.task.app.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import springboot.kotlin.task.app.congif.JwtProperties
import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.repository.UserRepository
import java.util.*

@Service
class JwtService(
    jwtProperties: JwtProperties,
    private val userRepository: UserRepository,
) {

    private val secret = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())
    private val expiration = jwtProperties.expiration

    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims(additionalClaims)
            .subject(userDetails.username)
            .issuedAt(Date())
            .expiration(expirationDate)
            .signWith(secret)
            .compact()

    fun extractUsername(token: String): String = getAllClaims(token).subject

    fun extractUserFromJwt(token: String): User {
        val jwt = token.removePrefix("Bearer ")
        val username = extractUsername(jwt)
        return userRepository.findByUsername(username) ?: throw IllegalArgumentException("Invalid user")
    }

    fun isExpired(token: String): Boolean = getAllClaims(token).expiration.before(Date())

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return userDetails.username == username && !isExpired(token)
    }


    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser().verifyWith(secret).build()
        return parser.parseSignedClaims(token).payload
    }
}
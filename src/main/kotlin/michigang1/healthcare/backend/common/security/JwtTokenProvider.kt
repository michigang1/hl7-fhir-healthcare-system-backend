package michigang1.healthcare.backend.common.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration}") private val jwtExpirationMs: Long
) {
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateToken(auth: Authentication): String {
        val username = auth.name
        val roles = auth.authorities.map { it.authority }
        val now = Date()
        val exp = Date(now.time + jwtExpirationMs)
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles
        // в JwtTokenProvider.generateToken():
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromToken(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject

    fun validateToken(token: String): Boolean =
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            false
        }
}

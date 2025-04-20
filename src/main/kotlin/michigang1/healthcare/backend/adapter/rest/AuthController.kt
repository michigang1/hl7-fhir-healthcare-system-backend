package michigang1.healthcare.backend.adapter.rest

import michigang1.healthcare.backend.domain.auth.payload.request.LoginRequest
import michigang1.healthcare.backend.domain.auth.payload.request.SignupRequest
import michigang1.healthcare.backend.domain.auth.payload.response.JwtResponse
import michigang1.healthcare.backend.domain.auth.payload.response.MessageResponse
import michigang1.healthcare.backend.domain.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(private val authService: AuthService) {

    @PostMapping("/signin")
    fun signin(@RequestBody request: LoginRequest): Mono<ResponseEntity<JwtResponse>> =
        authService.authenticate(request)
            .map { ResponseEntity.ok(it) }

    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequest): Mono<ResponseEntity<MessageResponse>> =
        authService.register(request)
            .map { ResponseEntity.ok(it) }
}

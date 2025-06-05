package michigang1.healthcare.backend.adapter.rest

import jakarta.validation.Valid
import michigang1.healthcare.backend.common.security.audit.AuditConfig
import michigang1.healthcare.backend.common.security.audit.repository.PersistentAuditEventRepository
import michigang1.healthcare.backend.domain.auth.payload.request.LoginRequest
import michigang1.healthcare.backend.domain.auth.payload.request.SignupRequest
import michigang1.healthcare.backend.domain.auth.payload.response.JwtResponse
import michigang1.healthcare.backend.domain.auth.payload.response.MessageResponse
import michigang1.healthcare.backend.domain.auth.service.AuthService
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@Validated
class AuthController(private val authService: AuthService,
                     private val auditConfig: AuditConfig,
                     private val auditEventRepository: PersistentAuditEventRepository) {

    @PostMapping("/api/v1/auth/signin")
    fun signin(@Valid @RequestBody request: LoginRequest): Mono<ResponseEntity<JwtResponse>> =
        authService.authenticate(request)
            .map { ResponseEntity.ok(it) }

    @PostMapping("/api/v1/auth/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): Mono<ResponseEntity<MessageResponse>> =
        authService.register(request)
            .map { ResponseEntity.ok(it) }

    @GetMapping ("/api/v1/actuator/auditevents")
    fun audit(): ResponseEntity<MutableList<AuditEvent>> {
       val response = auditConfig.auditEventRepository(auditEventRepository).find(null, null, null)
       return ResponseEntity.ok(response)
    }
}

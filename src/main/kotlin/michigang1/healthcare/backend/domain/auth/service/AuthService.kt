package michigang1.healthcare.backend.domain.auth.service


import michigang1.healthcare.backend.domain.auth.payload.request.LoginRequest
import michigang1.healthcare.backend.domain.auth.payload.request.SignupRequest
import michigang1.healthcare.backend.domain.auth.payload.response.JwtResponse
import michigang1.healthcare.backend.domain.auth.payload.response.MessageResponse
import reactor.core.publisher.Mono

interface AuthService {
    fun authenticate(request: LoginRequest): Mono<JwtResponse>
    fun register(request: SignupRequest): Mono<MessageResponse>
}

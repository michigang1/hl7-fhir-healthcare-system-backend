package michigang1.healthcare.backend.domain.auth.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.adapter.exception.EmailAlreadyExistsAuthException
import michigang1.healthcare.backend.adapter.exception.UsernameAlreadyExistsAuthException
import michigang1.healthcare.backend.common.security.JwtTokenProvider
import michigang1.healthcare.backend.common.security.audit.AuditLogger
import michigang1.healthcare.backend.domain.auth.model.RoleEnum
import michigang1.healthcare.backend.domain.auth.model.User
import michigang1.healthcare.backend.domain.auth.payload.request.LoginRequest
import michigang1.healthcare.backend.domain.auth.payload.request.SignupRequest
import michigang1.healthcare.backend.domain.auth.payload.response.JwtResponse
import michigang1.healthcare.backend.domain.auth.payload.response.MessageResponse
import michigang1.healthcare.backend.domain.auth.repository.RoleRepository
import michigang1.healthcare.backend.domain.auth.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import javax.management.relation.RoleNotFoundException

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val auditLogger: AuditLogger
) : AuthService {

    @Transactional
    override fun authenticate(request: LoginRequest): Mono<JwtResponse> =
        Mono.fromCallable {
            userRepository.findByUsername(request.username)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { user ->
                if (user == null || !passwordEncoder.matches(request.password, user.password)) {
                    return@flatMap Mono.error<JwtResponse>(BadCredentialsException("Invalid sign-in credentials"))
                }

                val authorities = user.roles.map { SimpleGrantedAuthority(it.name.name) }
                val auth = UsernamePasswordAuthenticationToken(user.username, user.password, authorities)
                val token = tokenProvider.generateToken(auth)

                Mono.just(JwtResponse(
                    token       = token,
                    id          = user.id,
                    username    = user.username,
                    email       = user.email,
                    roles       = user.roles.map { it.name.name }
                ))
            }
            .doOnSuccess { it?.let { response -> auditLogger.loginSuccess(response.username) } }
            .doOnError { auditLogger.loginFailure(request.username) }

    @Transactional
    override fun register(request: SignupRequest): Mono<MessageResponse> =
        Mono.fromCallable {
            if (userRepository.existsByUsername(request.username)) {
                throw UsernameAlreadyExistsAuthException()
            }
            if (userRepository.existsByEmail(request.email)) {
                throw EmailAlreadyExistsAuthException()
            }

            val wanted = request.roles.map { RoleEnum.valueOf(it) }
            val found   = roleRepository.findAllByNameIn(wanted)
            if (found.size != wanted.size) {
                println("Wanted: $wanted")
                println("Found: $found")
                val missing = wanted.toSet() - found.map { it.name }.toSet()
                throw RoleNotFoundException("Roles not found: $missing")
            }

            val user = User(
                username = request.username,
                email    = request.email,
                password = passwordEncoder.encode(request.password),
                roles    = found.toSet()
            )
            userRepository.save(user)

            MessageResponse("User registered successfully!")
        }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSuccess { auditLogger.registerSuccess(request.username) }
            .doOnError { auditLogger.registerFailure(request.username) }

}

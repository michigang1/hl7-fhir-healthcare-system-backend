package michigang1.healthcare.backend.common.security

import michigang1.healthcare.backend.domain.auth.repository.UserRepository
import org.springframework.context.annotation.Primary
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
@Primary
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> =
        Mono.fromCallable { userRepository.findByUsername(username) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { u ->
                if (u == null) Mono.empty()
                else {
                    val authorities: List<GrantedAuthority> = u.roles.map { SimpleGrantedAuthority(it.name.name) }
                    Mono.just(User(u.username, u.password, authorities))
                }
            }
}

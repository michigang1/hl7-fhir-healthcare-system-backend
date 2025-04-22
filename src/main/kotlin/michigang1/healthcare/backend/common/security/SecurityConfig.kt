package michigang1.healthcare.backend.common.security


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig(
    private val tokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsServiceImpl,

) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .csrf { it.disable() }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint { exchange, _ ->
                    Mono.fromRunnable {
                        exchange.response.statusCode = org.springframework.http.HttpStatus.UNAUTHORIZED
                    }
                }
            }
            .authorizeExchange { authorize ->
                authorize
                    .pathMatchers("/api/v1/auth/**").permitAll()
                    .pathMatchers("/api/v1/health").permitAll()
                    .pathMatchers("/api/v1/user").authenticated()
                    .pathMatchers("/api/v1/user/**").authenticated()
                    .pathMatchers("/api/v1/health").permitAll()
                    .pathMatchers("/api/v1/health/**").permitAll()
                    .anyExchange().authenticated()
            }

            .addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
        return http.build()
    }


    private fun bearerAuthenticationFilter(): WebFilter = WebFilter { exchange, chain ->
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.removePrefix("Bearer ").trim()
            if (tokenProvider.validateToken(token)) {
                val username = tokenProvider.getUsernameFromToken(token)
                return@WebFilter userDetailsService.findByUsername(username)
                    .flatMap { user ->
                        val auth = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                        chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                    }
            }
        }
        chain.filter(exchange)
    }
}

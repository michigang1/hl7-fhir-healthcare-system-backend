package michigang1.healthcare.backend.domain.auth.payload.request

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val roles: Set<String> = setOf()
)

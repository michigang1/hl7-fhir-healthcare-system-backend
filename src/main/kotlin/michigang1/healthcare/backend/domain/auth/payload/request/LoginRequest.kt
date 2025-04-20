package michigang1.healthcare.backend.domain.auth.payload.request

data class LoginRequest(
    val username: String,
    val password: String,
)

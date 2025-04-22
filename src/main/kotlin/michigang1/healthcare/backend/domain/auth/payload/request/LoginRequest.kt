package michigang1.healthcare.backend.domain.auth.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:Pattern(
        regexp = "^[a-zA-Z0-9._-]{3,}$",
        message = "Username must be at least 3 characters long and can only contain letters, numbers, dots, underscores, and hyphens"
    )
    val username: String,

    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one letter, one special character, and be at least 8 characters long"
    )
    val password: String,
)

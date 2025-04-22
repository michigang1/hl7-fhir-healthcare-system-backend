package michigang1.healthcare.backend.domain.auth.payload.request

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
data class SignupRequest(
    @field:Pattern(
        regexp = "^[a-zA-Z0-9._-]{3,}$",
        message = "Username must be at least 3 characters long and can only contain letters, numbers, dots, underscores, and hyphens"
    )
    val username: String,


    @field:Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "Email must be a valid email address"
    )
    val email: String,

    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one letter, one special character, and be at least 8 characters long"
    )
    val password: String,

    @field:NotEmpty
    val roles: Set<String> = setOf()
)

package michigang1.healthcare.backend.domain.patient.payload

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class PatientRequest (
    @field:NotNull
    val id: Long = 0,
    @field:Pattern(
        regexp = "^[a-zA-Zа-яА-ЯёЁіїєґІЇЄҐ\\s-]+$",
        message = "Name must contain only letters, spaces, and hyphens"
    )
    val name: String?,
    @field:Pattern(
        regexp = "^[a-zA-Zа-яА-ЯёЁіїєґІЇЄҐ\\s-]+$",
        message = "Surname must contain only letters, spaces, and hyphens"
    )
    val surname: String?,

    @field:NotBlank
    val roomNo: String?,

    @field:Pattern(
        regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
        message = "Date of birth must be in the format YYYY-MM-DD"
    )
    val dateOfBirth: String?,
    //male,female,diverse
    @field:Pattern(
        regexp = "^(male|female|diverse)$",
        message = "Gender must be 'male' , 'female ' or 'diverse'"
    )
    val gender: String?,
    @field:Pattern(
        regexp = "^[a-zA-Zа-яА-ЯёЁіїєґІЇЄҐ0-9\\s,.-]+$",
        message = "Address must contain only letters, numbers, spaces, commas, periods, and hyphens"
    )
    val address: String?,

    @field:Pattern(
        regexp = "^[a-zA-Zа-яА-ЯёЁіїєґІЇЄҐ0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "Email must be in a valid format"
    )
    val email: String?,
    @field:Pattern(
        regexp = "^\\+?[0-9]{10,15}$",
        message = "Phone number must be in a valid format"
    )
    val phone: String?,

    val identifier: Long?,

    val organizationId: Long?,
)

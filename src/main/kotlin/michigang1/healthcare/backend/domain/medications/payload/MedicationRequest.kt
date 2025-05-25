package michigang1.healthcare.backend.domain.medications.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class MedicationRequest(
    @field:NotNull(message = "patientId is required")
    val patientId: Long? = null,

    @field:NotBlank(message = "medicationName must not be blank")
    val medicationName: String?,

    @field:NotBlank(message = "dosage must not be blank")
    val dosage: String?,

    @field:Pattern(
        // 1-0-0-1 Mo, 1-0-0-2 Tu Fr, etc.
        regexp = "^[\\p{L}0-9 .,;\\-]{1,100}\$",
        message = "Dosage must contain valid characters (e.g. 1-0-0-1 Mo, 1-0-0-2 Tu Fr)"
    )
    val frequency: String?,

    @field:NotNull(message = "startDate is required")
    val startDate: LocalDate?,

    val endDate: LocalDate? = null,

    @field:NotBlank(message = "prescribedBy must not be blank")
    val prescribedBy: String?
)
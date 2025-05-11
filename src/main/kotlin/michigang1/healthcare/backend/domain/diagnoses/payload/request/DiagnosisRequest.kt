package michigang1.healthcare.backend.domain.diagnoses.payload.request

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
data class DiagnosisRequest(
    @field:NotNull(message = "patientId must be provided")
    val id: Long?,

    @field:NotBlank(message = "diagnosisCode must not be blank")
    @field:Pattern(
        regexp = "^[A-TV-Z][0-9]{2}(?:\\.[0-9A-TV-Z]{1,4})?\$",
        message = "Diagnosis code must be a valid ICD-10 code (e.g. A01.1)"
    )
    val code: String?,

    @field:NotNull(message = "isPrimary must be provided")
    val isPrimary: Boolean? = false,

    @field:NotBlank(message = "description must not be blank")
    @field:Pattern(
        regexp = "^[\\p{L}0-9 .,;\\-]{1,500}\$",
        message = "Description must contain valid characters"
    )
    val description: String?,

    @field:NotNull(message = "dateDiagnosed must be provided")
    @field:PastOrPresent(message = "Date of diagnose must be valid")
    val diagnosedAt: LocalDate?,


    @field:NotBlank(message = "diagnosedBy must not be blank")
    @field:Pattern(
        regexp = "^[\\p{L} .,-]{1,50}\$",
        message = "Diagnosed by must contain valid characters"
    )
    val diagnosedBy: String?,
)
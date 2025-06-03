package michigang1.healthcare.backend.domain.event.payload.request

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
data class EventRequest(
    @field:NotBlank(message = "Name must not be blank")
    @field:Pattern(
        regexp = "^[\\p{L}0-9 .,;\\-]{1,100}$",
        message = "Name must contain valid characters and be less than 100 characters"
    )
    val name: String?,

    @field:NotBlank(message = "Description must not be blank")
    @field:Pattern(
        regexp = "^[\\p{L}0-9 .,;\\-]{1,1000}$",
        message = "Description must contain valid characters and be less than 1000 characters"
    )
    val description: String?,

    @field:NotNull(message = "Author ID must be provided")
    val authorId: Long?,

    @field:NotNull(message = "Event date and time must be provided")
    val eventDateTime: LocalDateTime?,

    @field:NotEmpty(message = "At least one patient must be provided")
    val patientIds: Set<Long>?
)
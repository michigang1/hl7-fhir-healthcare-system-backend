package michigang1.healthcare.backend.domain.event.payload.response

import michigang1.healthcare.backend.domain.patient.payload.PatientResponse
import java.time.LocalDateTime

data class EventResponse(
    val id: Long,
    val name: String,
    val description: String,
    val authorId: Long,
    val authorUsername: String,
    val eventDateTime: LocalDateTime,
    val patients: List<PatientResponse>
)
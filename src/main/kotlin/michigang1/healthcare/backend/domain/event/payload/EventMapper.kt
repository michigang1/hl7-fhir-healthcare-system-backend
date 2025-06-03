package michigang1.healthcare.backend.domain.event.payload

import michigang1.healthcare.backend.domain.auth.model.User
import michigang1.healthcare.backend.domain.event.model.Event
import michigang1.healthcare.backend.domain.event.payload.request.EventRequest
import michigang1.healthcare.backend.domain.event.payload.response.EventResponse
import michigang1.healthcare.backend.domain.patient.model.Patient
import michigang1.healthcare.backend.domain.patient.payload.PatientMapper
import org.springframework.stereotype.Component

@Component
class EventMapper(private val patientMapper: PatientMapper) {

    fun toEntity(author: User, patients: Set<Patient>, request: EventRequest): Event =
        Event(
            name = request.name ?: throw IllegalArgumentException("Name is required"),
            description = request.description ?: throw IllegalArgumentException("Description is required"),
            author = author,
            eventDateTime = request.eventDateTime ?: throw IllegalArgumentException("Event date and time is required"),
            patients = patients
        )

    fun toResponse(event: Event): EventResponse =
        EventResponse(
            id = event.id,
            name = event.name,
            description = event.description,
            authorId = event.author.id,
            authorUsername = event.author.username,
            eventDateTime = event.eventDateTime,
            patients = event.patients.map { patientMapper.toResponse(it) }
        )
}

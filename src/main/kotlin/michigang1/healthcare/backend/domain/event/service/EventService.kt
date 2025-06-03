package michigang1.healthcare.backend.domain.event.service

import michigang1.healthcare.backend.domain.event.payload.request.EventRequest
import michigang1.healthcare.backend.domain.event.payload.response.EventResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventService {
    fun getAllEvents(): Flux<List<EventResponse>>
    fun getEventById(id: Long): Mono<EventResponse?>
    fun getEventsByPatient(patientId: Long): Flux<List<EventResponse>>
    fun createEvent(eventRequest: EventRequest): Mono<EventResponse>
    fun updateEvent(id: Long, eventRequest: EventRequest): Mono<EventResponse?>
    fun deleteEvent(id: Long): Mono<Boolean>
}
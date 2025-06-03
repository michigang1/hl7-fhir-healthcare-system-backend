package michigang1.healthcare.backend.adapter.rest

import michigang1.healthcare.backend.domain.event.payload.request.EventRequest
import michigang1.healthcare.backend.domain.event.payload.response.EventResponse
import michigang1.healthcare.backend.domain.event.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@Validated
class EventController(private val eventService: EventService) {

    @GetMapping("/api/v1/events")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllEvents(): Mono<ResponseEntity<List<EventResponse>>> =
        eventService.getAllEvents()
            .flatMapIterable { it }
            .collectList()
            .map { ResponseEntity.ok(it) }

    @GetMapping("/api/v1/events/{id}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getEventById(@PathVariable id: Long): Mono<ResponseEntity<EventResponse>> =
        eventService.getEventById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/api/v1/events/{id}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun updateEvent(
        @PathVariable id: Long,
        @RequestBody @Validated eventRequest: EventRequest
    ): Mono<ResponseEntity<EventResponse>> =
        eventService.updateEvent(id, eventRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/api/v1/events/{id}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun deleteEvent(@PathVariable id: Long): Mono<ResponseEntity<Boolean>> =
        eventService.deleteEvent(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @GetMapping("/api/v1/patients/{patientId}/events")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getEventsByPatient(@PathVariable patientId: Long): Mono<ResponseEntity<List<EventResponse>>> =
        eventService.getEventsByPatient(patientId)
            .flatMapIterable { it }
            .collectList()
            .map { ResponseEntity.ok(it) }

    @PostMapping("/api/v1/patients/{patientId}/events")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun createEvent(
        @PathVariable patientId: Long,
        @RequestBody @Validated eventRequest: EventRequest
    ): Mono<ResponseEntity<EventResponse>> =
        // Ensure the patient ID is included in the event request
        eventService.createEvent(eventRequest.copy(patientIds = eventRequest.patientIds?.plus(patientId) ?: setOf(patientId)))
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())
}

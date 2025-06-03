package michigang1.healthcare.backend.domain.event.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.auth.repository.UserRepository
import michigang1.healthcare.backend.domain.event.model.Event
import michigang1.healthcare.backend.domain.event.payload.EventMapper
import michigang1.healthcare.backend.domain.event.payload.request.EventRequest
import michigang1.healthcare.backend.domain.event.payload.response.EventResponse
import michigang1.healthcare.backend.domain.event.repository.EventRepository
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.NoSuchElementException

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val userRepository: UserRepository,
    private val patientRepository: PatientRepository
) : EventService {

    override fun getAllEvents(): Flux<List<EventResponse>> {
        return Mono.fromCallable { eventRepository.findAll() }
            .subscribeOn(Schedulers.boundedElastic())
            .map { events ->
                events.map { eventMapper.toResponse(it) }
            }
            .flux()
    }

    override fun getEventById(id: Long): Mono<EventResponse?> {
        return Mono.fromCallable { eventRepository.findById(id).orElse(null) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { event -> event?.let { eventMapper.toResponse(it) } }
    }

    override fun getEventsByPatient(patientId: Long): Flux<List<EventResponse>> {
        return Mono.fromCallable { eventRepository.findByPatientsId(patientId) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { events ->
                events.map { eventMapper.toResponse(it) }
            }
            .flux()
    }

    @Transactional
    override fun createEvent(eventRequest: EventRequest): Mono<EventResponse> {
        val authorId = eventRequest.authorId ?: return Mono.error(IllegalArgumentException("Author ID is required"))
        val patientIds = eventRequest.patientIds ?: return Mono.error(IllegalArgumentException("Patient IDs are required"))

        return Mono.fromCallable {
            val author = userRepository.findById(authorId).orElseThrow { NoSuchElementException("Author not found") }
            val patients = patientRepository.findAllById(patientIds)
                .filterNotNull()
                .toSet()

            if (patients.isEmpty()) {
                throw NoSuchElementException("No patients found with the provided IDs")
            }

            val event = eventMapper.toEntity(author, patients, eventRequest)
            eventRepository.save(event)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .map { eventMapper.toResponse(it) }
    }

    @Transactional
    override fun updateEvent(id: Long, eventRequest: EventRequest): Mono<EventResponse?> {
        return Mono.fromCallable {
            val existingEvent = eventRepository.findById(id).orElseThrow { NoSuchElementException("Event not found") }

            val authorId = eventRequest.authorId ?: existingEvent.author.id
            val author = userRepository.findById(authorId).orElseThrow { NoSuchElementException("Author not found") }

            val patientIds = eventRequest.patientIds ?: existingEvent.patients.map { it.id }.toSet()
            val patients = patientRepository.findAllById(patientIds)
                .filterNotNull()
                .toSet()

            if (patients.isEmpty()) {
                throw NoSuchElementException("No patients found with the provided IDs")
            }

            val updatedEvent = Event(
                id = existingEvent.id,
                name = eventRequest.name ?: existingEvent.name,
                description = eventRequest.description ?: existingEvent.description,
                author = author,
                eventDateTime = eventRequest.eventDateTime ?: existingEvent.eventDateTime,
                patients = patients
            )

            eventRepository.save(updatedEvent)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .map { eventMapper.toResponse(it) }
    }

    @Transactional
    override fun deleteEvent(id: Long): Mono<Boolean> {
        return Mono.fromCallable {
            val existingEvent = eventRepository.findById(id).orElseThrow { NoSuchElementException("Event not found") }
            eventRepository.delete(existingEvent)
            true
        }
        .subscribeOn(Schedulers.boundedElastic())
    }
}

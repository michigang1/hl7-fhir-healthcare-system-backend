package michigang1.healthcare.backend.domain.event.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.common.security.audit.AuditLogger
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
    private val patientRepository: PatientRepository,
    private val auditLogger: AuditLogger
) : EventService {
    private val defaultUser = "system" // Default user for audit logging

    override fun getAllEvents(): Flux<List<EventResponse>> {
        return Mono.fromCallable { eventRepository.findAllWithAuthor() }
            .subscribeOn(Schedulers.boundedElastic())
            .map { events ->
                val responses = events.map { eventMapper.toResponse(it) }
                // Log each event retrieval
                responses.forEach { response ->
                    response.id?.let { eventId ->
                        auditLogger.eventRetrieved(eventId, defaultUser)
                    }
                }
                responses
            }
            .doOnError { auditLogger.eventRetrievalFailed(defaultUser) }
            .flux()
    }

    override fun getEventById(id: Long): Mono<EventResponse?> {
        return Mono.fromCallable { eventRepository.findByIdWithAuthor(id) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { event -> event?.let { eventMapper.toResponse(it) } }
            .doOnSuccess { response -> 
                response?.id?.let { eventId -> 
                    auditLogger.eventRetrieved(eventId, defaultUser) 
                }
            }
            .doOnError { auditLogger.eventRetrievalFailed(defaultUser) }
    }

    override fun getEventsByPatient(patientId: Long): Flux<List<EventResponse>> {
        return Mono.fromCallable { eventRepository.findByPatientsIdWithAuthor(patientId) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { events ->
                val responses = events.map { eventMapper.toResponse(it) }
                // Log each event retrieval
                responses.forEach { response ->
                    response.id?.let { eventId ->
                        auditLogger.eventRetrieved(eventId, defaultUser)
                    }
                }
                responses
            }
            .doOnError { auditLogger.eventRetrievalFailed(defaultUser) }
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
        .doOnSuccess { response -> 
            response.id?.let { eventId -> 
                auditLogger.eventCreated(eventId, defaultUser) 
            }
        }
        .doOnError { auditLogger.eventCreationFailed(defaultUser) }
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
        .doOnSuccess { response -> 
            response?.id?.let { eventId -> 
                auditLogger.eventUpdated(eventId, defaultUser) 
            }
        }
        .doOnError { auditLogger.eventUpdateFailed(id, defaultUser) }
    }

    @Transactional
    override fun deleteEvent(id: Long): Mono<Boolean> {
        return Mono.fromCallable {
            val existingEvent = eventRepository.findById(id).orElseThrow { NoSuchElementException("Event not found") }
            eventRepository.delete(existingEvent)
            true
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { success -> 
            if (success) {
                auditLogger.eventDeleted(id, defaultUser)
            }
        }
        .doOnError { auditLogger.eventDeletionFailed(id, defaultUser) }
    }
}

package michigang1.healthcare.backend.domain.event.repository

import michigang1.healthcare.backend.domain.event.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EventRepository : JpaRepository<Event, Long> {
    fun findByPatientsId(patientId: Long): List<Event>
    fun findByAuthorId(authorId: Long): List<Event>
    
    @Query("SELECT e FROM Event e JOIN e.patients p WHERE p.id = :patientId AND e.id = :eventId")
    fun findByPatientIdAndEventId(patientId: Long, eventId: Long): Event?
}
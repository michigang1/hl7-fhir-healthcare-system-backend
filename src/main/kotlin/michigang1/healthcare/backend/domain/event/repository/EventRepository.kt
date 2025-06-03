package michigang1.healthcare.backend.domain.event.repository

import michigang1.healthcare.backend.domain.event.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EventRepository : JpaRepository<Event, Long> {
    fun findByPatientsId(patientId: Long): List<Event>
    fun findByAuthorId(authorId: Long): List<Event>

    @Query("SELECT e FROM Event e JOIN e.patients p WHERE p.id = :patientId AND e.id = :eventId")
    fun findByPatientIdAndEventId(patientId: Long, eventId: Long): Event?

    @Query("SELECT e FROM Event e JOIN FETCH e.author LEFT JOIN FETCH e.patients WHERE e.id = :id")
    fun findByIdWithAuthor(id: Long): Event?

    @Query("SELECT DISTINCT e FROM Event e JOIN FETCH e.author LEFT JOIN FETCH e.patients")
    fun findAllWithAuthor(): List<Event>

    @Query("SELECT DISTINCT e FROM Event e JOIN FETCH e.author LEFT JOIN FETCH e.patients JOIN e.patients p WHERE p.id = :patientId")
    fun findByPatientsIdWithAuthor(patientId: Long): List<Event>
}

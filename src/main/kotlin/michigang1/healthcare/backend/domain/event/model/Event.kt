package michigang1.healthcare.backend.domain.event.model

import jakarta.persistence.*
import michigang1.healthcare.backend.domain.auth.model.User
import michigang1.healthcare.backend.domain.patient.model.Patient
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, length = 1000)
    val description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @Column(nullable = false)
    val eventDateTime: LocalDateTime = LocalDateTime.now(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "event_patients",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "patient_id")]
    )
    val patients: Set<Patient> = emptySet()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (eventDateTime != other.eventDateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + eventDateTime.hashCode()
        return result
    }
}
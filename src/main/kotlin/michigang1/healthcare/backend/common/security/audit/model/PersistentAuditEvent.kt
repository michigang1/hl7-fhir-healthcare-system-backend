package michigang1.healthcare.backend.common.security.audit.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "persistent_audit_event")
data class PersistentAuditEvent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "principal", nullable = false)
    val principal: String,
    @Column(name = "event_date", nullable = false, columnDefinition = "TIMESTAMP")
    val eventDate: Instant,
    @Column(name = "event_type", nullable = false)
    val eventType: String,
)

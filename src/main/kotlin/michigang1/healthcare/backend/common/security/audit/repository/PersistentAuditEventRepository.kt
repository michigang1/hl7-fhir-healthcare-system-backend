package michigang1.healthcare.backend.common.security.audit.repository

import michigang1.healthcare.backend.common.security.audit.model.PersistentAuditEvent
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface PersistentAuditEventRepository : JpaRepository<PersistentAuditEvent, Long> {
    fun findByPrincipal(principal: String): List<PersistentAuditEvent>
    fun findByEventType(eventType: String): List<PersistentAuditEvent>
    fun findByEventDateBetween(startDate: Instant, endDate: Instant): List<PersistentAuditEvent>
}
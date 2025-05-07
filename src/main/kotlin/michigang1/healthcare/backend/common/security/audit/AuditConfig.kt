package michigang1.healthcare.backend.common.security.audit

import michigang1.healthcare.backend.common.security.audit.model.PersistentAuditEvent
import michigang1.healthcare.backend.common.security.audit.repository.PersistentAuditEventRepository
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.boot.actuate.audit.AuditEventRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

@Configuration
class AuditConfig {
    @Bean
    fun auditEventRepository(auditRepo: PersistentAuditEventRepository): AuditEventRepository =
        object : AuditEventRepository {
            override fun add(event: AuditEvent?) {
                auditRepo.save(
                    PersistentAuditEvent(
                        principal = event?.principal ?: "",
                        eventDate = event?.timestamp ?: Instant.now(),
                        eventType = event?.data.toString()
                    )
                )
            }

            override fun find(principal: String?, after: Instant?, type: String?): MutableList<AuditEvent> {
                val events = auditRepo.findAll()
                return events.map {
                    PersistentAuditEvent(
                        principal = it.principal,
                        eventDate = it.eventDate,
                        eventType = it.eventType
                    ).toAuditEvent()
                }.toMutableList()
            }
        }

    fun PersistentAuditEvent.toAuditEvent(): AuditEvent {
        return AuditEvent(
            this.principal,
            this.eventDate.toString(),
            this.eventType
        )
    }
}
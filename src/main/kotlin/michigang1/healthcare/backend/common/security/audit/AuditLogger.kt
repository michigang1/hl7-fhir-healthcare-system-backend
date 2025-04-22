package michigang1.healthcare.backend.common.security.audit

import michigang1.healthcare.backend.common.security.audit.repository.PersistentAuditEventRepository
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AuditLogger(private val persistentAuditEventRepository: PersistentAuditEventRepository, private val auditConfig: AuditConfig) {

    fun loginSuccess(username: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
              username,
              Instant.now().toString(),
              "LOGIN_STATUS=success",
                "user=$username",
            )
        )
    }

    fun loginFailure(username: String) {
      auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                username,
                Instant.now().toString(),
                "LOGIN_STATUS=failure",
                "user=$username",
            )
        )
    }

    fun registerSuccess(username: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                username,
                Instant.now().toString(),
                "REGISTER_STATUS=success",
                "user=$username",
            )
        )
    }

    fun registerFailure(username: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                username,
                Instant.now().toString(),
                "REGISTER_STATUS=failure",
                "user=$username",
            )
        )
    }

    fun patientCreated(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_CREATED=success",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }


}
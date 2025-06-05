package michigang1.healthcare.backend.common.security.audit

import michigang1.healthcare.backend.common.security.audit.repository.PersistentAuditEventRepository
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AuditLogger(private val persistentAuditEventRepository: PersistentAuditEventRepository, private val auditConfig: AuditConfig) {

    // Auth operations
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

    // Patient operations
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

    fun patientCreationFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_CREATED=failure",
                "user=$byUser",
            )
        )
    }

    fun patientUpdated(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_UPDATED=success",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }

    fun patientUpdateFailed(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_UPDATED=failure",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }

    fun patientDeleted(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_DELETED=success",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }

    fun patientDeletionFailed(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_DELETED=failure",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }

    fun patientRetrieved(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_RETRIEVED=success",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }

    fun patientRetrievalFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "PATIENT_RETRIEVED=failure",
                "user=$byUser",
            )
        )
    }

    // Organization operations
    fun organizationRetrieved(organizationId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "ORGANIZATION_RETRIEVED=success",
                "user=$byUser",
                "organizationId=$organizationId",
            )
        )
    }

    fun organizationRetrievalFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "ORGANIZATION_RETRIEVED=failure",
                "user=$byUser",
            )
        )
    }

    // Event operations
    fun eventCreated(eventId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_CREATED=success",
                "user=$byUser",
                "eventId=$eventId",
            )
        )
    }

    fun eventCreationFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_CREATED=failure",
                "user=$byUser",
            )
        )
    }

    fun eventUpdated(eventId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_UPDATED=success",
                "user=$byUser",
                "eventId=$eventId",
            )
        )
    }

    fun eventUpdateFailed(eventId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_UPDATED=failure",
                "user=$byUser",
                "eventId=$eventId",
            )
        )
    }

    fun eventDeleted(eventId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_DELETED=success",
                "user=$byUser",
                "eventId=$eventId",
            )
        )
    }

    fun eventDeletionFailed(eventId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_DELETED=failure",
                "user=$byUser",
                "eventId=$eventId",
            )
        )
    }

    fun eventRetrieved(eventId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_RETRIEVED=success",
                "user=$byUser",
                "eventId=$eventId",
            )
        )
    }

    fun eventRetrievalFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "EVENT_RETRIEVED=failure",
                "user=$byUser",
            )
        )
    }

    // Diagnosis operations
    fun diagnosisCreated(diagnosisId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_CREATED=success",
                "user=$byUser",
                "diagnosisId=$diagnosisId",
                "patientId=$patientId",
            )
        )
    }

    fun diagnosisCreationFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_CREATED=failure",
                "user=$byUser",
            )
        )
    }

    fun diagnosisUpdated(diagnosisId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_UPDATED=success",
                "user=$byUser",
                "diagnosisId=$diagnosisId",
                "patientId=$patientId",
            )
        )
    }

    fun diagnosisUpdateFailed(diagnosisId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_UPDATED=failure",
                "user=$byUser",
                "diagnosisId=$diagnosisId",
                "patientId=$patientId",
            )
        )
    }

    fun diagnosisDeleted(diagnosisId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_DELETED=success",
                "user=$byUser",
                "diagnosisId=$diagnosisId",
                "patientId=$patientId",
            )
        )
    }

    fun diagnosisDeletionFailed(diagnosisId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_DELETED=failure",
                "user=$byUser",
                "diagnosisId=$diagnosisId",
                "patientId=$patientId",
            )
        )
    }

    fun diagnosisRetrieved(diagnosisId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_RETRIEVED=success",
                "user=$byUser",
                "diagnosisId=$diagnosisId",
                "patientId=$patientId",
            )
        )
    }

    fun diagnosisRetrievalFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "DIAGNOSIS_RETRIEVED=failure",
                "user=$byUser",
            )
        )
    }

    // Medication operations
    fun medicationCreated(medicationId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_CREATED=success",
                "user=$byUser",
                "medicationId=$medicationId",
                "patientId=$patientId",
            )
        )
    }

    fun medicationCreationFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_CREATED=failure",
                "user=$byUser",
            )
        )
    }

    fun medicationUpdated(medicationId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_UPDATED=success",
                "user=$byUser",
                "medicationId=$medicationId",
                "patientId=$patientId",
            )
        )
    }

    fun medicationUpdateFailed(medicationId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_UPDATED=failure",
                "user=$byUser",
                "medicationId=$medicationId",
                "patientId=$patientId",
            )
        )
    }

    fun medicationDeleted(medicationId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_DELETED=success",
                "user=$byUser",
                "medicationId=$medicationId",
                "patientId=$patientId",
            )
        )
    }

    fun medicationDeletionFailed(medicationId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_DELETED=failure",
                "user=$byUser",
                "medicationId=$medicationId",
                "patientId=$patientId",
            )
        )
    }

    fun medicationRetrieved(medicationId: Long, patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_RETRIEVED=success",
                "user=$byUser",
                "medicationId=$medicationId",
                "patientId=$patientId",
            )
        )
    }

    fun medicationRetrievalFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "MEDICATION_RETRIEVED=failure",
                "user=$byUser",
            )
        )
    }

    // CarePlan operations
    fun carePlanGoalCreated(goalId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_GOAL_CREATED=success",
                "user=$byUser",
                "goalId=$goalId",
            )
        )
    }

    fun carePlanGoalCreationFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_GOAL_CREATED=failure",
                "user=$byUser",
            )
        )
    }

    fun carePlanGoalUpdated(goalId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_GOAL_UPDATED=success",
                "user=$byUser",
                "goalId=$goalId",
            )
        )
    }

    fun carePlanGoalUpdateFailed(goalId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_GOAL_UPDATED=failure",
                "user=$byUser",
                "goalId=$goalId",
            )
        )
    }

    fun carePlanGoalDeleted(goalId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_GOAL_DELETED=success",
                "user=$byUser",
                "goalId=$goalId",
            )
        )
    }

    fun carePlanGoalDeletionFailed(goalId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_GOAL_DELETED=failure",
                "user=$byUser",
                "goalId=$goalId",
            )
        )
    }

    fun carePlanMeasureCreated(goalId: Long, measureId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_MEASURE_CREATED=success",
                "user=$byUser",
                "goalId=$goalId",
                "measureId=$measureId",
            )
        )
    }

    fun carePlanMeasureCreationFailed(goalId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_MEASURE_CREATED=failure",
                "user=$byUser",
                "goalId=$goalId",
            )
        )
    }

    fun carePlanMeasureUpdated(goalId: Long, measureId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_MEASURE_UPDATED=success",
                "user=$byUser",
                "goalId=$goalId",
                "measureId=$measureId",
            )
        )
    }

    fun carePlanMeasureUpdateFailed(goalId: Long, measureId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_MEASURE_UPDATED=failure",
                "user=$byUser",
                "goalId=$goalId",
                "measureId=$measureId",
            )
        )
    }

    fun carePlanMeasureDeleted(goalId: Long, measureId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_MEASURE_DELETED=success",
                "user=$byUser",
                "goalId=$goalId",
                "measureId=$measureId",
            )
        )
    }

    fun carePlanMeasureDeletionFailed(goalId: Long, measureId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_MEASURE_DELETED=failure",
                "user=$byUser",
                "goalId=$goalId",
                "measureId=$measureId",
            )
        )
    }

    fun carePlanRetrieved(patientId: Long, byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_RETRIEVED=success",
                "user=$byUser",
                "patientId=$patientId",
            )
        )
    }

    fun carePlanRetrievalFailed(byUser: String) {
        auditConfig.auditEventRepository(persistentAuditEventRepository).add(
            AuditEvent(
                byUser,
                Instant.now().toString(),
                "CAREPLAN_RETRIEVED=failure",
                "user=$byUser",
            )
        )
    }
}

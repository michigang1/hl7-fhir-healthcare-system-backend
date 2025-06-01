package michigang1.healthcare.backend.domain.careplan.payload

import java.time.LocalDateTime

data class CarePlanResponse(
    val id: Long,
    val patientId: Long,
    val patientName: String,
    val measureId: Long,
    val measureName: String,
    val measureDescription: String,
    val goalId: Long,
    val goalName: String,
    val goalDescription: String,
    val goalDuration: String,
    val goalFrequency: String,
    val scheduledDateTime: LocalDateTime,
    val isCompleted: Boolean,
    val isGoalAchieved: Boolean,
    val notes: String?
)
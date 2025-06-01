package michigang1.healthcare.backend.domain.careplan.payload

import java.time.LocalDateTime

data class CarePlanRequest(
    val patientId: Long? = null,
    val measureId: Long? = null,
    val goalId: Long? = null,
    val scheduledDateTime: LocalDateTime? = null,
    val isCompleted: Boolean? = false,
    val isGoalAchieved: Boolean? = false,
    val notes: String? = null,
    
    // For creating a new measure directly
    val measureName: String? = null,
    val measureDescription: String? = null,
    val measureTemplateId: Long? = null,
    
    // For creating a new goal directly
    val goalName: String? = null,
    val goalDescription: String? = null,
    val goalDuration: String? = null,
    val goalFrequency: String? = null,
    val goalTemplateId: Long? = null
)
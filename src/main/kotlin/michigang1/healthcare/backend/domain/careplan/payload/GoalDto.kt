package michigang1.healthcare.backend.domain.careplan.payload

data class GoalDto(
    val id: Long,
    val name: String,
    val description: String,
    val patientId: Long
)

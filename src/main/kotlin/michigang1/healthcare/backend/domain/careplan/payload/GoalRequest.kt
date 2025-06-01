package michigang1.healthcare.backend.domain.careplan.payload

data class GoalRequest(
    val name: String? = null,
    val description: String? = null,
    val duration: String? = null,
    val frequency: String? = null,
    val templateId: Long? = null
)
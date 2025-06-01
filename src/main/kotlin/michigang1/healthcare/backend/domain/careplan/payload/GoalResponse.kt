package michigang1.healthcare.backend.domain.careplan.payload

data class GoalResponse(
    val id: Long,
    val name: String,
    val description: String,
    val duration: String,
    val frequency: String,
    val templateId: Long? = null,
    val templateName: String? = null
)
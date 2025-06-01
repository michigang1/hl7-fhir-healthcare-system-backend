package michigang1.healthcare.backend.domain.careplan.payload

data class MeasureResponse(
    val id: Long,
    val name: String,
    val description: String,
    val templateId: Long? = null,
    val templateName: String? = null
)
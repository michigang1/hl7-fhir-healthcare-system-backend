package michigang1.healthcare.backend.domain.careplan.payload

data class MeasureRequest(
    val name: String? = null,
    val description: String? = null,
    val templateId: Long? = null
)
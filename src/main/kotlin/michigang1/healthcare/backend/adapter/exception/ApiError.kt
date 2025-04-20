package michigang1.healthcare.backend.adapter.exception

data class ApiError(
    val status: Int,
    val error: String,
    val description: String
)
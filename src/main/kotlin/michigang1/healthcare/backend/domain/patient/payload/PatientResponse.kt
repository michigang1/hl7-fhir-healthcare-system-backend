package michigang1.healthcare.backend.domain.patient.payload

data class PatientResponse (
    val id: Long?,
    val name: String?,
    val surname: String?,
    val dateOfBirth: String?,
    val gender: String?,
    val address: String?,
    val email: String?,
    val phone: String?,
    val identifier: Long?,
    val organizationId: Long?,
)
package michigang1.healthcare.backend.domain.medications.payload

import java.time.LocalDate

data class MedicationResponse(
    val id: Long,
    val patientId: Long,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val prescribedBy: String
)
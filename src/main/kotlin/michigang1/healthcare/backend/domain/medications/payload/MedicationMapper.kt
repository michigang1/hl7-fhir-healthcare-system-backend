package michigang1.healthcare.backend.domain.medications.payload


import michigang1.healthcare.backend.domain.medications.Medication
import michigang1.healthcare.backend.domain.patient.model.Patient
import michigang1.healthcare.backend.domain.patient.payload.PatientMapper
import org.springframework.stereotype.Component

@Component
class MedicationMapper(
    private val patientMapper: PatientMapper
) {
    fun toEntity(request: MedicationRequest, patientEntity: Patient): Medication =
        Medication(
            patient       = patientEntity,
            medicationName = request.medicationName!!,
            dosage         = request.dosage!!,
            frequency      = request.frequency!!,
            startDate      = request.startDate!!,
            endDate        = request.endDate,
            prescribedBy   = request.prescribedBy!!
        )

    fun toResponse(entity: Medication): MedicationResponse =
        MedicationResponse(
            id             = entity.id,
            patientId      = entity.patient.id,
            medicationName = entity.medicationName,
            dosage         = entity.dosage,
            frequency      = entity.frequency,
            startDate      = entity.startDate,
            endDate        = entity.endDate,
            prescribedBy   = entity.prescribedBy
        )
}
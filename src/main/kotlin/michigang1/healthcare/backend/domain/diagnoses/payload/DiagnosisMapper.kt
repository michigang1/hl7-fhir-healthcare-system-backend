package michigang1.healthcare.backend.domain.diagnoses.payload

import michigang1.healthcare.backend.domain.diagnoses.Diagnosis
import michigang1.healthcare.backend.domain.diagnoses.payload.request.DiagnosisRequest
import michigang1.healthcare.backend.domain.diagnoses.payload.response.DiagnosisResponse
import michigang1.healthcare.backend.domain.patient.model.Patient
import org.springframework.stereotype.Component

@Component
class DiagnosisMapper {

    fun toEntity(patient: Patient, request: DiagnosisResponse): Diagnosis =
        Diagnosis(
            // id не передаём — оно сгенерируется при сохранении
            patient     = patient,
            code        = request.code,
            isPrimary   = request.isPrimary,
            description = request.description,
            diagnosedAt = request.diagnosedAt,
            diagnosedBy = request.diagnosedBy
        )

    fun toResponse(diagnosis: Diagnosis): DiagnosisResponse =
        DiagnosisResponse(
            id           = diagnosis.id,
            patientId    = diagnosis.patient.id,
            code         = diagnosis.code,
            isPrimary    = diagnosis.isPrimary,
            description  = diagnosis.description,
            diagnosedAt  = diagnosis.diagnosedAt,
            diagnosedBy  = diagnosis.diagnosedBy
        )
}
package michigang1.healthcare.backend.domain.diagnoses.service

import michigang1.healthcare.backend.domain.diagnoses.payload.request.DiagnosisRequest
import michigang1.healthcare.backend.domain.diagnoses.payload.response.DiagnosisResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DiagnosisService {
    fun getAllDiagnoses(): Flux<List<DiagnosisResponse>>
    fun getAllDiagnosesByPatient(patientId: Long): Flux<List<DiagnosisResponse>>
    fun getDiagnosisByPatient(patientId: Long, id: Long): Mono<DiagnosisResponse?>
    fun createDiagnosis(diagnosisRequest: DiagnosisRequest): Mono<DiagnosisResponse>
    fun updateDiagnosis(
        patientId: Long,
        id: Long,
        diagnosisRequest: DiagnosisRequest
    ): Mono<DiagnosisResponse?>

    fun deleteDiagnosis(patientId: Long, id: Long): Mono<Boolean>
}
package michigang1.healthcare.backend.domain.diagnoses.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.diagnoses.model.Diagnosis
import michigang1.healthcare.backend.domain.diagnoses.payload.DiagnosisMapper
import michigang1.healthcare.backend.domain.diagnoses.payload.request.DiagnosisRequest
import michigang1.healthcare.backend.domain.diagnoses.payload.response.DiagnosisResponse
import michigang1.healthcare.backend.domain.diagnoses.repository.DiagnosisRepository
import michigang1.healthcare.backend.domain.patient.payload.PatientMapper
import michigang1.healthcare.backend.domain.patient.service.PatientService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class DiagnosisServiceImp(
    private val diagnosisRepository: DiagnosisRepository,
    private val diagnosisMapper: DiagnosisMapper,
    private val patientMapper: PatientMapper,
    private val patientService: PatientService,
): DiagnosisService {
    override fun getAllDiagnoses(): Flux<List<DiagnosisResponse>> {
        return Flux.just(diagnosisRepository.findAll().map {
            DiagnosisResponse(
                id = it.id,
                patientId = it.patient.id,
                code = it.code,
                isPrimary = it.isPrimary,
                description = it.description,
                diagnosedAt = it.diagnosedAt,
                diagnosedBy = it.diagnosedBy
            )
        })
    }

    override fun getAllDiagnosesByPatient(patientId: Long): Flux<List<DiagnosisResponse>> {
        return Flux.just(diagnosisRepository.findByPatientId(patientId).map {
            DiagnosisResponse(
                id = it.id,
                patientId = it.patient.id,
                code = it.code,
                isPrimary = it.isPrimary,
                description = it.description,
                diagnosedAt = it.diagnosedAt,
                diagnosedBy = it.diagnosedBy
            )
        })
    }

    override fun getDiagnosisByPatient(patientId: Long, id: Long): Mono<DiagnosisResponse?> {
        return Mono.fromCallable { diagnosisRepository.findByPatientIdAndId(patientId, id) ?: throw NoSuchElementException("Diagnosis not found") }
            .map {
                    DiagnosisResponse(
                        id = it.id,
                        patientId = it.patient.id,
                        code = it.code,
                        isPrimary = it.isPrimary,
                        description = it.description,
                        diagnosedAt = it.diagnosedAt,
                        diagnosedBy = it.diagnosedBy
                    )
            }
    }

    @Transactional
    override fun createDiagnosis(
        request: DiagnosisRequest
    ): Mono<DiagnosisResponse> {
        val patientId = request.id ?: return Mono.error(IllegalArgumentException("Patient ID is required"))

        return patientService.getById(patientId)
            .switchIfEmpty(Mono.error(NoSuchElementException("Patient not found")))
            .flatMap { patient ->
                val diagnosis = Diagnosis(
                    patient     = patientMapper.toEntity(patient!!),
                    code        = request.code ?: return@flatMap Mono.error(IllegalArgumentException("Code is required")),
                    isPrimary   = request.isPrimary ?: return@flatMap Mono.error(IllegalArgumentException("isPrimary is required")),
                    description = request.description ?: return@flatMap Mono.error(IllegalArgumentException("Description is required")),
                    diagnosedAt = request.diagnosedAt ?: return@flatMap Mono.error(IllegalArgumentException("DiagnosedAt is required")),
                    diagnosedBy = request.diagnosedBy ?: return@flatMap Mono.error(IllegalArgumentException("DiagnosedBy is required"))
                )
                Mono.fromCallable { diagnosisRepository.save(diagnosis) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .map { diagnosisMapper.toResponse(it) }
    }

    override fun updateDiagnosis(
        patientId: Long,
        id: Long,
        diagnosisRequest: DiagnosisRequest
    ): Mono<DiagnosisResponse?> {
        return Mono.fromCallable {
            val existingDiagnosis = diagnosisRepository.findByPatientIdAndId(patientId, id)
                ?: throw NoSuchElementException("Diagnosis not found")

           val updatedDiagnosis = Diagnosis(
                id          = existingDiagnosis.id,
                patient     = existingDiagnosis.patient,
                code        = diagnosisRequest.code ?: existingDiagnosis.code,
                isPrimary   = diagnosisRequest.isPrimary ?: existingDiagnosis.isPrimary,
                description = diagnosisRequest.description ?: existingDiagnosis.description,
                diagnosedAt = diagnosisRequest.diagnosedAt ?: existingDiagnosis.diagnosedAt,
                diagnosedBy = diagnosisRequest.diagnosedBy ?: existingDiagnosis.diagnosedBy
            )

            diagnosisRepository.save(updatedDiagnosis)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .map { diagnosisMapper.toResponse(it) }
    }

    override fun deleteDiagnosis(patientId: Long, id: Long): Mono<Boolean> {
        return Mono.fromCallable {
            val existingDiagnosis = diagnosisRepository.findByPatientIdAndId(patientId, id)
                ?: throw NoSuchElementException("Diagnosis not found")
            diagnosisRepository.delete(existingDiagnosis)
            true
        }
            .subscribeOn(Schedulers.boundedElastic())
    }
}
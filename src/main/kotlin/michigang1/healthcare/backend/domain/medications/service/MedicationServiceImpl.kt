package michigang1.healthcare.backend.domain.medications.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.common.security.audit.AuditLogger
import michigang1.healthcare.backend.domain.medications.Medication
import michigang1.healthcare.backend.domain.medications.payload.MedicationMapper
import michigang1.healthcare.backend.domain.medications.payload.MedicationRequest
import michigang1.healthcare.backend.domain.medications.payload.MedicationResponse
import michigang1.healthcare.backend.domain.medications.repository.MedicationRepository
import michigang1.healthcare.backend.domain.patient.payload.PatientMapper
import michigang1.healthcare.backend.domain.patient.service.PatientService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class MedicationServiceImp(
    private val medicationRepository: MedicationRepository,
    private val medicationMapper: MedicationMapper,
    private val patientService: PatientService,
    private val patientMapper: PatientMapper,
    private val auditLogger: AuditLogger
) : MedicationService {
    private val defaultUser = "system" // Default user for audit logging
    override fun getAllMedications(): Flux<List<MedicationResponse>> {
        return Mono.fromCallable { medicationRepository.findAll() }
            .subscribeOn(Schedulers.boundedElastic())
            .map { medications ->
                val responses = medications.map { medicationMapper.toResponse(it) }
                responses
            }
            .flux()
    }

    override fun getAllByPatient(patientId: Long): Flux<List<MedicationResponse>> {
        return Mono.fromCallable { medicationRepository.findByPatientId(patientId) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { medications ->
                val responses = medications.map { medicationMapper.toResponse(it) }
                responses
            }
            .flux()
    }


    override fun getMedicationByPatient(patientId: Long, id: Long): Mono<MedicationResponse> =
        Mono.fromCallable {
            medicationRepository.findByPatientIdAndId(patientId, id)
                ?: throw NoSuchElementException("Medication not found")
        }
            .subscribeOn(Schedulers.boundedElastic())
            .map { medicationMapper.toResponse(it) }

    @Transactional
    override fun createMedication(request: MedicationRequest): Mono<MedicationResponse> {
        val pid = request.patientId
            ?: return Mono.error(IllegalArgumentException("Patient ID is required"))

        return patientService.getById(pid)
            .switchIfEmpty(Mono.error(NoSuchElementException("Patient not found")))
            .flatMap { patientResp ->
                val patientEntity = patientMapper.toEntity(patientResp!!)
                val entity = medicationMapper.toEntity(request, patientEntity)
                Mono.fromCallable { medicationRepository.save(entity) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .map { medicationMapper.toResponse(it) }
            .doOnSuccess { response -> 
                response.id?.let { medicationId -> 
                    response.patientId?.let { patientId ->
                        auditLogger.medicationCreated(medicationId, patientId, defaultUser) 
                    }
                }
            }
            .doOnError { auditLogger.medicationCreationFailed(defaultUser) }
    }

    @Transactional
    override fun updateMedication(
        patientId: Long,
        id: Long,
        request: MedicationRequest
    ): Mono<MedicationResponse> =
        Mono.fromCallable {
            val existing = medicationRepository.findByPatientIdAndId(patientId, id)
                ?: throw NoSuchElementException("Medication not found")
            val updated = Medication(
                id             = existing.id,
                patient        = existing.patient,
                medicationName = request.medicationName ?: existing.medicationName,
                dosage         = request.dosage ?: existing.dosage,
                frequency      = request.frequency ?: existing.frequency,
                startDate      = request.startDate ?: existing.startDate,
                endDate        = request.endDate ?: existing.endDate,
                prescribedBy   = request.prescribedBy ?: existing.prescribedBy
            )
            medicationRepository.save(updated)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .map { medicationMapper.toResponse(it) }
            .doOnSuccess { response -> 
                response.id?.let { medicationId -> 
                    auditLogger.medicationUpdated(medicationId, patientId, defaultUser) 
                }
            }
            .doOnError { auditLogger.medicationUpdateFailed(id, patientId, defaultUser) }

    override fun deleteMedication(patientId: Long, id: Long): Mono<Boolean> =
        Mono.fromCallable {
            val existing = medicationRepository.findByPatientIdAndId(patientId, id)
                ?: throw NoSuchElementException("Medication not found")
            medicationRepository.delete(existing)
            true
        }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSuccess { success -> 
                if (success) {
                    auditLogger.medicationDeleted(id, patientId, defaultUser)
                }
            }
            .doOnError { auditLogger.medicationDeletionFailed(id, patientId, defaultUser) }
}

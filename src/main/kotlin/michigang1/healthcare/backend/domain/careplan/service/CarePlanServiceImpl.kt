package michigang1.healthcare.backend.domain.careplan.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.common.security.audit.AuditLogger
import michigang1.healthcare.backend.domain.careplan.model.Goal
import michigang1.healthcare.backend.domain.careplan.model.Measure
import michigang1.healthcare.backend.domain.careplan.payload.GoalDto
import michigang1.healthcare.backend.domain.careplan.payload.GoalMapper
import michigang1.healthcare.backend.domain.careplan.payload.MeasureDto
import michigang1.healthcare.backend.domain.careplan.payload.MeasureMapper
import michigang1.healthcare.backend.domain.careplan.repository.GoalRepository
import michigang1.healthcare.backend.domain.careplan.repository.MeasureRepository
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CarePlanServiceImpl(
    private val goalRepository: GoalRepository,
    private val measureRepository: MeasureRepository,
    private val patientRepository: PatientRepository,
    private val auditLogger: AuditLogger
): CarePlanService {
    private val defaultUser = "system" // Default user for audit logging

    @Transactional
    override fun getCarePlanByPatientId(patientId: Long): Mono<List<MeasureDto>> {
      return Mono.fromCallable {
            measureRepository.findAllWithGoalByPatientId(patientId).map { MeasureMapper.toDto(it) }
      }
      .subscribeOn(Schedulers.boundedElastic())
      .doOnSuccess { 
          auditLogger.carePlanRetrieved(patientId, defaultUser)
      }
      .doOnError { 
          auditLogger.carePlanRetrievalFailed(defaultUser)
      }
    }

    @Transactional
    override fun createGoal(goalDto: GoalDto): Mono<GoalDto> {
      return Mono.fromCallable {
            val patient = patientRepository.findById(goalDto.patientId).orElseThrow()
            val goal = goalRepository.save(
                Goal(
                    name = goalDto.name,
                    description = goalDto.description,
                    patient = patient!!
                ))
            GoalMapper.toDto(goal)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { response -> 
            response.id?.let { goalId -> 
                auditLogger.carePlanGoalCreated(goalId, defaultUser) 
            }
        }
        .doOnError { auditLogger.carePlanGoalCreationFailed(defaultUser) }
    }
    @Transactional
    override fun createMeasure(
        goalId: Long,
        dto: MeasureDto
    ): Mono<MeasureDto> {
       return Mono.fromCallable {
            val goal = goalRepository.findById(goalId).orElseThrow()
            val measure = measureRepository.save(
                Measure(
                    name = dto.name,
                    description = dto.description,
                    scheduledDateTime = dto.scheduledDateTime,
                    isCompleted = dto.isCompleted,
                    goal = goal
                )
            )
            MeasureMapper.toDto(measure)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { response -> 
            response.id?.let { measureId -> 
                auditLogger.carePlanMeasureCreated(goalId, measureId, defaultUser) 
            }
        }
        .doOnError { auditLogger.carePlanMeasureCreationFailed(goalId, defaultUser) }
    }

    override fun deleteGoal(goalId: Long): Mono<Unit> {
        return Mono.fromCallable { 
            goalRepository.deleteById(goalId) 
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { 
            auditLogger.carePlanGoalDeleted(goalId, defaultUser)
        }
        .doOnError { 
            auditLogger.carePlanGoalDeletionFailed(goalId, defaultUser)
        }
    }
    @Transactional
    override fun deleteMeasure(
        goalId: Long,
        measureId: Long
    ): Mono<Unit> {
        return Mono.fromCallable {
            goalRepository.findById(goalId).orElseThrow()
            measureRepository.findById(measureId).orElseThrow()
            measureRepository.deleteById(measureId)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { 
            auditLogger.carePlanMeasureDeleted(goalId, measureId, defaultUser)
        }
        .doOnError { 
            auditLogger.carePlanMeasureDeletionFailed(goalId, measureId, defaultUser)
        }
    }

    @Transactional
    override fun updateGoal(
        goalId: Long,
        goalDto: GoalDto
    ): Mono<GoalDto> {
        return Mono.fromCallable {
            val existingGoal = goalRepository.findById(goalId).orElseThrow()
            val updatedGoal = existingGoal.copy(
                name = goalDto.name,
                description = goalDto.description
            )
            goalRepository.save(updatedGoal)
            GoalMapper.toDto(updatedGoal)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { response -> 
            response.id?.let { goalId -> 
                auditLogger.carePlanGoalUpdated(goalId, defaultUser) 
            }
        }
        .doOnError { auditLogger.carePlanGoalUpdateFailed(goalId, defaultUser) }
    }

    @Transactional
    override fun updateMeasure(
        goalId: Long,
        measureId: Long,
        measureDto: MeasureDto
    ): Mono<MeasureDto> {
        return Mono.fromCallable {
            val existingGoal = goalRepository.findById(goalId).orElseThrow()
            val existingMeasure = measureRepository.findById(measureId).orElseThrow()
            val updatedMeasure = existingMeasure.copy(
                name = measureDto.name,
                description = measureDto.description,
                scheduledDateTime = measureDto.scheduledDateTime,
                isCompleted = measureDto.isCompleted,
                goal = measureDto.goalId.let { existingGoal } ?: existingMeasure.goal
            )
            measureRepository.save(updatedMeasure)
            MeasureMapper.toDto(updatedMeasure)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { response -> 
            response.id?.let { measureId -> 
                auditLogger.carePlanMeasureUpdated(goalId, measureId, defaultUser) 
            }
        }
        .doOnError { auditLogger.carePlanMeasureUpdateFailed(goalId, measureId, defaultUser) }
    }

    @Transactional
    override fun getGoalById(
        goalId: Long
    ): Mono<GoalDto?> {
        return Mono.fromCallable {
            goalRepository.findById(goalId).orElseThrow()
                .let { GoalMapper.toDto(it) }
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { response -> 
            response?.patientId?.let { patientId -> 
                auditLogger.carePlanRetrieved(patientId, defaultUser) 
            }
        }
        .doOnError { auditLogger.carePlanRetrievalFailed(defaultUser) }
    }

    @Transactional
    override fun getMeasureById(
        goalId: Long,
        measureId: Long
    ): Mono<MeasureDto?> {
        return Mono.fromCallable {
            val goal = goalRepository.findById(goalId).orElseThrow()
            measureRepository.findById(measureId).orElseThrow()
                .let { MeasureMapper.toDto(it) }
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess { response -> 
            response?.goalId?.let { 
                val goal = goalRepository.findById(goalId).orElse(null)
                goal?.patient?.id?.let { patientId ->
                    auditLogger.carePlanRetrieved(patientId, defaultUser) 
                }
            }
        }
        .doOnError { auditLogger.carePlanRetrievalFailed(defaultUser) }
    }

    @Transactional
    override fun getAllGoalsByPatient(patientId: Long): Mono<List<GoalDto>> {
       return Mono.fromCallable {
           goalRepository.findAllByPatientId(patientId)
               .map { GoalMapper.toDto(it) }
       }
       .subscribeOn(Schedulers.boundedElastic())
       .doOnSuccess { 
           auditLogger.carePlanRetrieved(patientId, defaultUser)
       }
       .doOnError { auditLogger.carePlanRetrievalFailed(defaultUser) }
    }
}

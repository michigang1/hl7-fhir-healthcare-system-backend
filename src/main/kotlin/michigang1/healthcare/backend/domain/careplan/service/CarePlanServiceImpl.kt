package michigang1.healthcare.backend.domain.careplan.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.model.Goal
import michigang1.healthcare.backend.domain.careplan.model.Measure
import michigang1.healthcare.backend.domain.careplan.payload.CarePlanMapper
import michigang1.healthcare.backend.domain.careplan.payload.CarePlanRequest
import michigang1.healthcare.backend.domain.careplan.payload.CarePlanResponse
import michigang1.healthcare.backend.domain.careplan.repository.CarePlanRepository
import michigang1.healthcare.backend.domain.careplan.repository.GoalRepository
import michigang1.healthcare.backend.domain.careplan.repository.GoalTemplateRepository
import michigang1.healthcare.backend.domain.careplan.repository.MeasureRepository
import michigang1.healthcare.backend.domain.careplan.repository.MeasureTemplateRepository
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.NoSuchElementException

@Service
class CarePlanServiceImpl(
    private val carePlanRepository: CarePlanRepository,
    private val measureRepository: MeasureRepository,
    private val goalRepository: GoalRepository,
    private val measureTemplateRepository: MeasureTemplateRepository,
    private val goalTemplateRepository: GoalTemplateRepository,
    private val patientRepository: PatientRepository
) : CarePlanService {

    override fun getAll(): Mono<List<CarePlanResponse>> {
        return Mono.fromCallable {
            carePlanRepository.findAll().map { CarePlanMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getById(id: Long): Mono<CarePlanResponse?> {
        return Mono.fromCallable {
            val carePlan = carePlanRepository.findById(id).orElse(null)
            carePlan?.let { CarePlanMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByPatientId(patientId: Long): Mono<List<CarePlanResponse>> {
        return Mono.fromCallable {
            carePlanRepository.findByPatientId(patientId).map { CarePlanMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun createCarePlan(carePlanRequest: CarePlanRequest): Mono<CarePlanResponse> {
        return Mono.fromCallable {
            // Get patient
            val patient = patientRepository.findById(carePlanRequest.patientId ?: 
                throw IllegalArgumentException("Patient ID is required"))
                .orElseThrow { NoSuchElementException("Patient not found") }!!

            // Get or create measure
            val measure = if (carePlanRequest.measureId != null) {
                measureRepository.findById(carePlanRequest.measureId)
                    .orElseThrow { NoSuchElementException("Measure not found") }!!
            } else if (carePlanRequest.measureName != null && carePlanRequest.measureDescription != null) {
                // Create new measure
                val template = carePlanRequest.measureTemplateId?.let {
                    measureTemplateRepository.findById(it)
                        .orElseThrow { NoSuchElementException("Measure template not found") }!!
                }

                val newMeasure = Measure(
                    name = carePlanRequest.measureName,
                    description = carePlanRequest.measureDescription,
                    template = template
                )
                measureRepository.save(newMeasure)
            } else {
                throw IllegalArgumentException("Either measure ID or measure details (name and description) are required")
            }

            // Get or create goal
            val goal = if (carePlanRequest.goalId != null) {
                goalRepository.findById(carePlanRequest.goalId)
                    .orElseThrow { NoSuchElementException("Goal not found") }!!
            } else if (carePlanRequest.goalName != null && carePlanRequest.goalDescription != null &&
                carePlanRequest.goalDuration != null && carePlanRequest.goalFrequency != null) {
                // Create new goal
                val template = carePlanRequest.goalTemplateId?.let {
                    goalTemplateRepository.findById(it)
                        .orElseThrow { NoSuchElementException("Goal template not found") }!!
                }

                val newGoal = Goal(
                    name = carePlanRequest.goalName,
                    description = carePlanRequest.goalDescription,
                    duration = carePlanRequest.goalDuration,
                    frequency = carePlanRequest.goalFrequency,
                    template = template
                )
                goalRepository.save(newGoal)
            } else {
                throw IllegalArgumentException("Either goal ID or goal details (name, description, duration, frequency) are required")
            }

            // Create care plan
            val carePlan = CarePlanMapper.toEntity(carePlanRequest, patient, measure, goal)
            val savedCarePlan = carePlanRepository.save(carePlan)

            CarePlanMapper.toResponse(savedCarePlan)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun updateCarePlan(id: Long, carePlanRequest: CarePlanRequest): Mono<CarePlanResponse?> {
        return Mono.fromCallable {
            val existingCarePlan = carePlanRepository.findById(id)
                .orElseThrow { NoSuchElementException("Care plan not found") }!!

            // Update measure if needed
            val measure = if (carePlanRequest.measureId != null && carePlanRequest.measureId != existingCarePlan.measure.id) {
                measureRepository.findById(carePlanRequest.measureId)
                    .orElseThrow { NoSuchElementException("Measure not found") }!!
            } else {
                null
            }

            // Update goal if needed
            val goal = if (carePlanRequest.goalId != null && carePlanRequest.goalId != existingCarePlan.goal.id) {
                goalRepository.findById(carePlanRequest.goalId)
                    .orElseThrow { NoSuchElementException("Goal not found") }!!
            } else {
                null
            }

            // Update care plan
            val updatedCarePlan = CarePlanMapper.updateEntity(existingCarePlan, carePlanRequest, measure, goal)
            val savedCarePlan = carePlanRepository.save(updatedCarePlan)

            CarePlanMapper.toResponse(savedCarePlan)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun deleteCarePlan(id: Long): Boolean {
        val carePlan = carePlanRepository.findById(id).orElseThrow {
            NoSuchElementException("Care plan with ID $id not found")
        }!!

        carePlanRepository.delete(carePlan)

        return !carePlanRepository.existsById(id)
    }
}

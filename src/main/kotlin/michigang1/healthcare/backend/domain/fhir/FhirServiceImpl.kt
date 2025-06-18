package michigang1.healthcare.backend.domain.fhir

import michigang1.healthcare.backend.adapter.exception.ResourceNotFoundException
import michigang1.healthcare.backend.common.util.fhir.*
import michigang1.healthcare.backend.domain.auth.repository.UserRepository
import michigang1.healthcare.backend.domain.careplan.service.CarePlanService
import michigang1.healthcare.backend.domain.diagnoses.repository.DiagnosisRepository
import michigang1.healthcare.backend.domain.event.repository.EventRepository
import michigang1.healthcare.backend.domain.medications.repository.MedicationRepository
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import michigang1.healthcare.backend.domain.careplan.repository.MeasureRepository
import org.hl7.fhir.r4.model.*
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
@Primary
class FhirServiceImpl(
    private val patientRepository: PatientRepository,
    private val diagnosisRepository: DiagnosisRepository,
    private val medicationRepository: MedicationRepository,
    private val carePlanService: CarePlanService,
    private val eventRepository: EventRepository,
    private val measureRepository: MeasureRepository,
    private val userRepository: UserRepository
) : FhirService {

    // Converters
    private val patientConverter = PatientConverter
    private val conditionConverter = ConditionConverter(patientRepository)
    private val medicationRequestConverter = MedicationRequestConverter(patientRepository)
    private val goalConverter = GoalConverter(measureRepository)
    private val encounterConverter = EncounterConverter(patientRepository, userRepository)
    private val carePlanConverter = CarePlanConverter

    // Patient resources
    override fun getPatientResource(patientId: Long): Mono<Patient> {
        return Mono.fromCallable {
            val patient = patientRepository.findById(patientId)
                .orElseThrow { ResourceNotFoundException("Patient not found with ID: $patientId") }
            patientConverter.toFhir(patient!!)
        }
    }

    override fun getAllPatientResources(): Mono<List<Patient>> {
        return Mono.fromCallable {
            patientRepository.findAll().filterNotNull().map { patientConverter.toFhir(it) }
        }
    }

    override fun createPatientResource(fhirPatient: Patient): Mono<Patient> {
        return Mono.fromCallable {
            val patient = patientConverter.fromFhir(fhirPatient)
            val savedPatient = patientRepository.save(patient)
            patientConverter.toFhir(savedPatient)
        }
    }

    // Condition resources (diagnoses)
    override fun getConditionResource(diagnosisId: Long): Mono<Condition> {
        return Mono.fromCallable {
            val diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow { ResourceNotFoundException("Diagnosis not found with ID: $diagnosisId") }
            conditionConverter.toFhir(diagnosis)
        }
    }

    override fun getConditionResourcesByPatient(patientId: Long): Mono<List<Condition>> {
        return Mono.fromCallable {
            val patient = patientRepository.findById(patientId)
                .orElseThrow { ResourceNotFoundException("Patient not found with ID: $patientId") }
            patient!!.diagnoses.map { conditionConverter.toFhir(it) }
        }
    }

    override fun createConditionResource(fhirCondition: Condition): Mono<Condition> {
        return Mono.fromCallable {
            val diagnosis = conditionConverter.fromFhir(fhirCondition)
            val savedDiagnosis = diagnosisRepository.save(diagnosis)
            conditionConverter.toFhir(savedDiagnosis)
        }
    }

    // MedicationRequest resources
    override fun getMedicationRequestResource(medicationId: Long): Mono<MedicationRequest> {
        return Mono.fromCallable {
            val medication = medicationRepository.findById(medicationId)
                .orElseThrow { ResourceNotFoundException("Medication not found with ID: $medicationId") }
            medicationRequestConverter.toFhir(medication)
        }
    }

    override fun getMedicationRequestResourcesByPatient(patientId: Long): Mono<List<MedicationRequest>> {
        return Mono.fromCallable {
            val patient = patientRepository.findById(patientId)
                .orElseThrow { ResourceNotFoundException("Patient not found with ID: $patientId") }
            patient!!.medications.map { medicationRequestConverter.toFhir(it) }
        }
    }

    override fun createMedicationRequestResource(fhirMedicationRequest: MedicationRequest): Mono<MedicationRequest> {
        return Mono.fromCallable {
            val medication = medicationRequestConverter.fromFhir(fhirMedicationRequest)
            val savedMedication = medicationRepository.save(medication)
            medicationRequestConverter.toFhir(savedMedication)
        }
    }

    // CarePlan and Goal resources
    override fun getCarePlanResource(patientId: Long): Mono<CarePlan> {
        return carePlanService.getAllGoalsByPatient(patientId)
            .map { goals ->
                carePlanConverter.toFhir(patientId, goals)
            }
    }

    override fun getGoalResource(goalId: Long): Mono<Goal> {
        return carePlanService.getGoalById(goalId)
            .map { goalDto ->
                if (goalDto == null) throw ResourceNotFoundException("Goal not found with ID: $goalId")
                goalConverter.toFhir(goalDto)
            }
    }

    override fun getGoalResourcesByPatient(patientId: Long): Mono<List<Goal>> {
        return carePlanService.getAllGoalsByPatient(patientId)
            .map { goals ->
                goals.map { goalConverter.toFhir(it) }
            }
    }

    override fun createGoalResource(fhirGoal: Goal): Mono<Goal> {
        val goalDto = goalConverter.fromFhir(fhirGoal)
        return carePlanService.createGoal(goalDto)
            .map { savedGoalDto -> goalConverter.toFhir(savedGoalDto) }
    }

    // Encounter resources (events)
    override fun getEncounterResource(eventId: Long): Mono<Encounter> {
        return Mono.fromCallable {
            val event = eventRepository.findByIdWithAuthor(eventId)
                ?: throw ResourceNotFoundException("Event not found with ID: $eventId")
            encounterConverter.toFhir(event)
        }
    }

    override fun getEncounterResourcesByPatient(patientId: Long): Mono<List<Encounter>> {
        return Mono.fromCallable {
            val events = eventRepository.findByPatientsIdWithAuthor(patientId)
            events.map { encounterConverter.toFhir(it) }
        }
    }

    override fun createEncounterResource(fhirEncounter: Encounter): Mono<Encounter> {
        return Mono.fromCallable {
            val event = encounterConverter.fromFhir(fhirEncounter)
            val savedEvent = eventRepository.save(event)
            encounterConverter.toFhir(savedEvent)
        }
    }
}

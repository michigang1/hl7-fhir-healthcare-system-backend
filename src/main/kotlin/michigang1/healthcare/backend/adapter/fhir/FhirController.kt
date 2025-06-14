package michigang1.healthcare.backend.adapter.fhir

import ca.uhn.fhir.parser.IParser
import michigang1.healthcare.backend.common.util.fhir.BundleConverter
import michigang1.healthcare.backend.domain.fhir.FhirService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/fhir")
class FhirController(
    private val fhirService: FhirService,
    private val fhirJsonParser: IParser
) {

    @GetMapping("/Patient/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPatient(@PathVariable id: Long): Mono<String> {
        return fhirService.getPatientResource(id)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @GetMapping("/Patient", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllPatients(): Mono<String> {
        return fhirService.getAllPatientResources()
            .map { patients -> 
                val bundle = BundleConverter.createSearchSetBundle(patients)
                fhirJsonParser.encodeResourceToString(bundle)
            }
    }

    @GetMapping("/Condition/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCondition(@PathVariable id: Long): Mono<String> {
        return fhirService.getConditionResource(id)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @GetMapping("/Patient/{patientId}/Condition", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPatientConditions(@PathVariable patientId: Long): Mono<String> {
        return fhirService.getConditionResourcesByPatient(patientId)
            .map { conditions -> 
                val bundle = BundleConverter.createSearchSetBundle(conditions)
                fhirJsonParser.encodeResourceToString(bundle)
            }
    }
    @GetMapping("/MedicationRequest/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMedicationRequest(@PathVariable id: Long): Mono<String> {
        return fhirService.getMedicationRequestResource(id)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @GetMapping("/Patient/{patientId}/MedicationRequest", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPatientMedicationRequests(@PathVariable patientId: Long): Mono<String> {
        return fhirService.getMedicationRequestResourcesByPatient(patientId)
            .map { medications -> 
                val bundle = BundleConverter.createSearchSetBundle(medications)
                fhirJsonParser.encodeResourceToString(bundle)
            }
    }

    @GetMapping("/Patient/{patientId}/CarePlan", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPatientCarePlan(@PathVariable patientId: Long): Mono<String> {
        return fhirService.getCarePlanResource(patientId)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @GetMapping("/Goal/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGoal(@PathVariable id: Long): Mono<String> {
        return fhirService.getGoalResource(id)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @GetMapping("/Patient/{patientId}/Goal", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPatientGoals(@PathVariable patientId: Long): Mono<String> {
        return fhirService.getGoalResourcesByPatient(patientId)
            .map { goals -> 
                val bundle = BundleConverter.createSearchSetBundle(goals)
                fhirJsonParser.encodeResourceToString(bundle)
            }
    }

    @GetMapping("/Encounter/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getEncounter(@PathVariable id: Long): Mono<String> {
        return fhirService.getEncounterResource(id)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @GetMapping("/Patient/{patientId}/Encounter", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPatientEncounters(@PathVariable patientId: Long): Mono<String> {
        return fhirService.getEncounterResourcesByPatient(patientId)
            .map { encounters -> 
                val bundle = BundleConverter.createSearchSetBundle(encounters)
                fhirJsonParser.encodeResourceToString(bundle)
            }
    }

    @PostMapping("/Patient", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createPatient(@RequestBody patientJson: String): Mono<String> {
        val fhirPatient = fhirJsonParser.parseResource(org.hl7.fhir.r4.model.Patient::class.java, patientJson)
        return fhirService.createPatientResource(fhirPatient)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @PostMapping("/Condition", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createCondition(@RequestBody conditionJson: String): Mono<String> {
        val fhirCondition = fhirJsonParser.parseResource(org.hl7.fhir.r4.model.Condition::class.java, conditionJson)
        return fhirService.createConditionResource(fhirCondition)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @PostMapping("/MedicationRequest", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createMedicationRequest(@RequestBody medicationRequestJson: String): Mono<String> {
        val fhirMedicationRequest = fhirJsonParser.parseResource(org.hl7.fhir.r4.model.MedicationRequest::class.java, medicationRequestJson)
        return fhirService.createMedicationRequestResource(fhirMedicationRequest)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @PostMapping("/Goal", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createGoal(@RequestBody goalJson: String): Mono<String> {
        val fhirGoal = fhirJsonParser.parseResource(org.hl7.fhir.r4.model.Goal::class.java, goalJson)
        return fhirService.createGoalResource(fhirGoal)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }

    @PostMapping("/Encounter", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createEncounter(@RequestBody encounterJson: String): Mono<String> {
        val fhirEncounter = fhirJsonParser.parseResource(org.hl7.fhir.r4.model.Encounter::class.java, encounterJson)
        return fhirService.createEncounterResource(fhirEncounter)
            .map { fhirJsonParser.encodeResourceToString(it) }
    }
}

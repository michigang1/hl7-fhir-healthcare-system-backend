package michigang1.healthcare.backend.domain.fhir

import reactor.core.publisher.Mono

interface FhirService {
    // Patient resources
    fun getPatientResource(patientId: Long): Mono<org.hl7.fhir.r4.model.Patient>
    fun getAllPatientResources(): Mono<List<org.hl7.fhir.r4.model.Patient>>
    fun createPatientResource(patient: org.hl7.fhir.r4.model.Patient): Mono<org.hl7.fhir.r4.model.Patient>

    // Condition resources (diagnoses)
    fun getConditionResource(diagnosisId: Long): Mono<org.hl7.fhir.r4.model.Condition>
    fun getConditionResourcesByPatient(patientId: Long): Mono<List<org.hl7.fhir.r4.model.Condition>>
    fun createConditionResource(condition: org.hl7.fhir.r4.model.Condition): Mono<org.hl7.fhir.r4.model.Condition>

    // MedicationRequest resources
    fun getMedicationRequestResource(medicationId: Long): Mono<org.hl7.fhir.r4.model.MedicationRequest>
    fun getMedicationRequestResourcesByPatient(patientId: Long): Mono<List<org.hl7.fhir.r4.model.MedicationRequest>>
    fun createMedicationRequestResource(medicationRequest: org.hl7.fhir.r4.model.MedicationRequest): Mono<org.hl7.fhir.r4.model.MedicationRequest>

    // CarePlan and Goal resources
    fun getCarePlanResource(patientId: Long): Mono<org.hl7.fhir.r4.model.CarePlan>
    fun getGoalResource(goalId: Long): Mono<org.hl7.fhir.r4.model.Goal>
    fun getGoalResourcesByPatient(patientId: Long): Mono<List<org.hl7.fhir.r4.model.Goal>>
    fun createGoalResource(goal: org.hl7.fhir.r4.model.Goal): Mono<org.hl7.fhir.r4.model.Goal>

    // Encounter resources (events)
    fun getEncounterResource(eventId: Long): Mono<org.hl7.fhir.r4.model.Encounter>
    fun getEncounterResourcesByPatient(patientId: Long): Mono<List<org.hl7.fhir.r4.model.Encounter>>
    fun createEncounterResource(encounter: org.hl7.fhir.r4.model.Encounter): Mono<org.hl7.fhir.r4.model.Encounter>
}

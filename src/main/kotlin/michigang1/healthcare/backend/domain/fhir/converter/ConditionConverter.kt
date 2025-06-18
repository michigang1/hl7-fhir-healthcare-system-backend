package michigang1.healthcare.backend.common.util.fhir

import michigang1.healthcare.backend.domain.diagnoses.model.Diagnosis
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Reference
import java.time.LocalDate
import java.util.*

class ConditionConverter(private val patientRepository: PatientRepository) {
    fun toFhir(diagnosis: Diagnosis): Condition {
        val condition = Condition()

        // Set ID
        condition.id = diagnosis.id.toString()

        // Set subject (patient)
        condition.subject = Reference("Patient/" + diagnosis.patient.id)

        // Set code
        val codeableConcept = CodeableConcept()
        val coding = Coding()
        coding.system = "http://terminology.hl7.org/CodeSystem/icd10"
        coding.code = diagnosis.code
        coding.display = diagnosis.description
        codeableConcept.addCoding(coding)
        condition.code = codeableConcept

        // Set clinical status
        val clinicalStatus = CodeableConcept()
        val statusCoding = Coding()
        statusCoding.system = "http://terminology.hl7.org/CodeSystem/condition-clinical"
        statusCoding.code = "active"
        statusCoding.display = "Active"
        clinicalStatus.addCoding(statusCoding)
        condition.clinicalStatus = clinicalStatus

        // Set recorded date
        condition.recordedDate = DateConverter.convertLocalDateToDate(diagnosis.diagnosedAt)

        // Set recorder
        val recorder = Reference()
        recorder.display = diagnosis.diagnosedBy
        condition.recorder = recorder

        return condition
    }

    fun fromFhir(fhirCondition: Condition): Diagnosis {
        // Extract data from FHIR Condition
        val id = if (fhirCondition.hasId()) fhirCondition.idElement.idPart.toLongOrNull() ?: 0L else 0L

        // Extract patient reference
        val patientId = fhirCondition.subject.reference
            .replace("Patient/", "")
            .toLongOrNull() ?: throw IllegalArgumentException("Invalid patient reference")
        val patient = patientRepository.findById(patientId)
            .orElseThrow { NoSuchElementException("Patient not found") }!!

        // Extract code
        val code = fhirCondition.code.coding.firstOrNull()?.code ?: ""

        // Extract description
        val description = fhirCondition.code.coding.firstOrNull()?.display ?: ""

        // Extract diagnosed date
        val diagnosedAt = if (fhirCondition.hasRecordedDate()) {
            val date = fhirCondition.recordedDate
            DateConverter.convertDateToLocalDate(date)
        } else {
            LocalDate.now()
        }

        // Extract diagnosed by
        val diagnosedBy = fhirCondition.recorder?.display ?: "Unknown"

        // Create and return Diagnosis domain object
        return Diagnosis(
            id = id,
            patient = patient,
            code = code,
            isPrimary = false, // Default value
            description = description,
            diagnosedAt = diagnosedAt,
            diagnosedBy = diagnosedBy
        )
    }
}

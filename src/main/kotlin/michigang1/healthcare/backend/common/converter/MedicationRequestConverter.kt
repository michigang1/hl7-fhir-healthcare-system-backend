package michigang1.healthcare.backend.common.util.fhir

import michigang1.healthcare.backend.domain.medications.Medication
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Dosage
import org.hl7.fhir.r4.model.MedicationRequest
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Timing
import java.time.LocalDate

class MedicationRequestConverter(private val patientRepository: PatientRepository) {
    fun toFhir(medication: Medication): MedicationRequest {
        val medicationRequest = MedicationRequest()

        // Set ID
        medicationRequest.id = medication.id.toString()

        // Set status
        medicationRequest.status = MedicationRequest.MedicationRequestStatus.ACTIVE

        // Set intent
        medicationRequest.intent = MedicationRequest.MedicationRequestIntent.ORDER

        // Set medication
        val medicationCodeableConcept = CodeableConcept()
        medicationCodeableConcept.text = medication.medicationName
        medicationRequest.medication = medicationCodeableConcept

        // Set subject (patient)
        medicationRequest.subject = Reference("Patient/" + medication.patient.id)

        // Set authored on date
        medicationRequest.authoredOn = DateConverter.convertLocalDateToDate(medication.startDate)

        // Set requester
        val requester = Reference()
        requester.display = medication.prescribedBy
        medicationRequest.requester = requester

        // Set dosage instructions
        val dosage = Dosage()
        dosage.text = medication.dosage

        val timing = Timing()
        val timingCode = CodeableConcept()
        timingCode.text = medication.frequency
        timing.code = timingCode
        dosage.timing = timing

        medicationRequest.addDosageInstruction(dosage)

        return medicationRequest
    }

    fun fromFhir(fhirMedicationRequest: MedicationRequest): Medication {
        // Extract data from FHIR MedicationRequest
        val id = if (fhirMedicationRequest.hasId()) fhirMedicationRequest.idElement.idPart.toLongOrNull() ?: 0L else 0L

        // Extract patient reference
        val patientId = fhirMedicationRequest.subject.reference
            .replace("Patient/", "")
            .toLongOrNull() ?: throw IllegalArgumentException("Invalid patient reference")
        val patient = patientRepository.findById(patientId)
            .orElseThrow { NoSuchElementException("Patient not found") }!!

        // Extract medication name
        val medicationName = when (val medication = fhirMedicationRequest.medication) {
            is CodeableConcept -> medication.text ?: ""
            else -> ""
        }

        // Extract dosage
        val dosage = fhirMedicationRequest.dosageInstruction.firstOrNull()?.text ?: ""

        // Extract frequency
        val frequency = fhirMedicationRequest.dosageInstruction.firstOrNull()?.timing?.code?.text ?: ""

        // Extract start date
        val startDate = if (fhirMedicationRequest.hasAuthoredOn()) {
            val date = fhirMedicationRequest.authoredOn
            DateConverter.convertDateToLocalDate(date)
        } else {
            LocalDate.now()
        }

        // Extract prescribed by
        val prescribedBy = fhirMedicationRequest.requester?.display ?: "Unknown"

        // Create and return Medication domain object
        return Medication(
            id = id,
            patient = patient,
            medicationName = medicationName,
            dosage = dosage,
            frequency = frequency,
            startDate = startDate,
            endDate = null, // Default value
            prescribedBy = prescribedBy
        )
    }
}
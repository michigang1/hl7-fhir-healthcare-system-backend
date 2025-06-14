package michigang1.healthcare.backend.common.util.fhir

import michigang1.healthcare.backend.domain.auth.repository.UserRepository
import michigang1.healthcare.backend.domain.event.model.Event
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.hl7.fhir.r4.model.*
import java.time.LocalDateTime
import java.util.*

class EncounterConverter(
    private val patientRepository: PatientRepository,
    private val userRepository: UserRepository
) {
    fun toFhir(event: Event): Encounter {
        val encounter = Encounter()

        // Set ID
        encounter.id = event.id.toString()

        // Set status
        encounter.status = Encounter.EncounterStatus.FINISHED

        // Set class
        val encounterClass = Coding()
        encounterClass.system = "http://terminology.hl7.org/CodeSystem/v3-ActCode"
        encounterClass.code = "AMB"
        encounterClass.display = "ambulatory"
        encounter.class_ = encounterClass

        // Set subject (patient) - using the first patient if there are multiple
        if (event.patients.isNotEmpty()) {
            encounter.subject = Reference("Patient/${event.patients.first().id}")

            // Add all patients as participants
            event.patients.forEach { patient ->
                val participation = Encounter.EncounterParticipantComponent()
                participation.individual = Reference("Patient/${patient.id}")
                encounter.addParticipant(participation)
            }
        }

        // Set period
        val period = Period()
        period.start = DateConverter.convertLocalDateTimeToDate(event.eventDateTime)
        encounter.period = period

        // Set reason
        val reasonCode = CodeableConcept()
        reasonCode.text = event.name
        encounter.addReasonCode(reasonCode)

        return encounter
    }

    fun fromFhir(fhirEncounter: Encounter): Event {
        // Extract data from FHIR Encounter
        val id = if (fhirEncounter.hasId()) fhirEncounter.idElement.idPart.toLongOrNull() ?: 0L else 0L

        // Extract name (reason)
        val name = fhirEncounter.reasonCode.firstOrNull()?.text ?: ""

        // Extract description
        val description = name // Using name as description for now

        // Extract event date time
        val eventDateTime = if (fhirEncounter.hasPeriod() && fhirEncounter.period.hasStart()) {
            val date = fhirEncounter.period.start
            DateConverter.convertDateToLocalDateTime(date)
        } else {
            LocalDateTime.now()
        }

        // Extract patients
        val patientIds = fhirEncounter.participant.mapNotNull { participant ->
            participant.individual?.reference
                ?.replace("Patient/", "")
                ?.toLongOrNull()
        }
        val patients = patientRepository.findAllById(patientIds)
            .filterNotNull()
            .toSet()

        // Get a default user for author (this should be replaced with actual user authentication)
        val author = userRepository.findById(1L)
            .orElseThrow { NoSuchElementException("Default user not found") }!!

        // Create and return Event domain object
        return Event(
            id = id,
            name = name,
            description = description,
            author = author,
            eventDateTime = eventDateTime,
            patients = patients
        )
    }
}

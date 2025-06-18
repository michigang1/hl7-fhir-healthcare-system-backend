package michigang1.healthcare.backend.common.util.fhir

import michigang1.healthcare.backend.domain.patient.model.Patient
import org.hl7.fhir.r4.model.ContactPoint
import org.hl7.fhir.r4.model.Enumerations
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Address
import java.time.LocalDate

object PatientConverter {
    fun toFhir(patient: Patient): org.hl7.fhir.r4.model.Patient {
        val fhirPatient = org.hl7.fhir.r4.model.Patient()

        // Set ID
        fhirPatient.id = patient.id.toString()

        // Set identifier
        val identifier = Identifier()
        identifier.system = "urn:oid:1.2.36.146.595.217.0.1"
        identifier.value = patient.identifier.toString()
        fhirPatient.addIdentifier(identifier)

        // Set active status
        fhirPatient.active = true

        // Set name
        val name = HumanName()
        name.family = patient.lastName
        name.addGiven(patient.firstName)
        fhirPatient.addName(name)

        // Set gender
        fhirPatient.gender = when (patient.gender.lowercase()) {
            "male" -> Enumerations.AdministrativeGender.MALE
            "female" -> Enumerations.AdministrativeGender.FEMALE
            "other" -> Enumerations.AdministrativeGender.OTHER
            else -> Enumerations.AdministrativeGender.UNKNOWN
        }

        // Set birth date
        try {
            fhirPatient.birthDate = DateConverter.parseDate(patient.birthDate)
        } catch (e: Exception) {
            // If date parsing fails, log the error but continue
            println("Error parsing birth date: ${e.message}")
        }

        // Set telecom (phone)
        val phone = ContactPoint()
        phone.system = ContactPoint.ContactPointSystem.PHONE
        phone.value = patient.phone
        fhirPatient.addTelecom(phone)

        // Set telecom (email)
        val email = ContactPoint()
        email.system = ContactPoint.ContactPointSystem.EMAIL
        email.value = patient.email
        fhirPatient.addTelecom(email)

        // Set address
        val address = Address()
        address.text = patient.address
        fhirPatient.addAddress(address)

        return fhirPatient
    }

    fun fromFhir(fhirPatient: org.hl7.fhir.r4.model.Patient): Patient {
        // Extract data from FHIR Patient
        val id = if (fhirPatient.hasId()) fhirPatient.idElement.idPart.toLongOrNull() ?: 0L else 0L

        // Extract name
        val name = fhirPatient.name.firstOrNull()
        val firstName = name?.given?.firstOrNull()?.toString() ?: ""
        val lastName = name?.family ?: ""

        // Extract gender
        val gender = when (fhirPatient.gender) {
            Enumerations.AdministrativeGender.MALE -> "male"
            Enumerations.AdministrativeGender.FEMALE -> "female"
            Enumerations.AdministrativeGender.OTHER -> "other"
            else -> "unknown"
        }

        // Extract birth date
        val birthDate = fhirPatient.birthDate?.toString() ?: LocalDate.now().toString()

        // Extract contact info
        val phone = fhirPatient.telecom
            .firstOrNull { it.system == ContactPoint.ContactPointSystem.PHONE }
            ?.value ?: ""
        val email = fhirPatient.telecom
            .firstOrNull { it.system == ContactPoint.ContactPointSystem.EMAIL }
            ?.value ?: ""

        // Extract address
        val address = fhirPatient.address.firstOrNull()?.text ?: ""

        // Extract identifier
        val identifier = fhirPatient.identifier
            .firstOrNull()
            ?.value?.toLongOrNull() ?: 0L

        // Create and return Patient domain object
        return Patient(
            id = id,
            firstName = firstName,
            lastName = lastName,
            roomNo = "TBD", // Default value, should be updated with actual room number
            birthDate = birthDate,
            gender = gender,
            address = address,
            email = email,
            phone = phone,
            identifier = identifier,
            organizationId = 1L // Default value, should be updated with actual organization ID
        )
    }
}
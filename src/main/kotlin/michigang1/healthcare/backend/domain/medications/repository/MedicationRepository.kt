package michigang1.healthcare.backend.domain.medications.repository

import michigang1.healthcare.backend.domain.medications.Medication
import org.springframework.data.jpa.repository.JpaRepository

interface MedicationRepository : JpaRepository<Medication, Long> {
    fun findByPatientId(patientId: Long): List<Medication>
    fun findByPatientIdAndId(patientId: Long, id: Long): Medication?
}
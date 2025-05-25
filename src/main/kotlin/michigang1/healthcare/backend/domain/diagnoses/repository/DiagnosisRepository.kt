package michigang1.healthcare.backend.domain.diagnoses.repository

import michigang1.healthcare.backend.domain.diagnoses.model.Diagnosis
import org.springframework.data.jpa.repository.JpaRepository

interface DiagnosisRepository: JpaRepository<Diagnosis, Long> {
    fun findByPatientId(patientId: Long): List<Diagnosis>
    fun findByPatientIdAndId(patientId: Long, id: Long): Diagnosis?
    fun deleteByPatientIdAndId(patientId: Long, id: Long): Boolean
}
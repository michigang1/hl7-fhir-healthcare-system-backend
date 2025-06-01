package michigang1.healthcare.backend.domain.careplan.repository

import michigang1.healthcare.backend.domain.careplan.model.CarePlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CarePlanRepository : JpaRepository<CarePlan, Long> {
    fun findByPatientId(patientId: Long): List<CarePlan>
}
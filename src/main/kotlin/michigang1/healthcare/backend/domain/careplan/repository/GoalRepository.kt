package michigang1.healthcare.backend.domain.careplan.repository

import michigang1.healthcare.backend.domain.careplan.model.Goal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : JpaRepository<Goal, Long>{
    fun findAllByPatientId(patientId: Long): List<Goal>
}
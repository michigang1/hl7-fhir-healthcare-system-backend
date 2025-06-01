package michigang1.healthcare.backend.domain.careplan.repository

import michigang1.healthcare.backend.domain.careplan.model.Measure
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MeasureRepository : JpaRepository<Measure, Long> {

    @Query("""
    SELECT m FROM Measure m 
    JOIN FETCH m.goal g 
    WHERE g.patient.id = :patientId
""")
    fun findAllWithGoalByPatientId(@Param("patientId") patientId: Long): List<Measure>

}
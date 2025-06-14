package michigang1.healthcare.backend.common.util.fhir

import michigang1.healthcare.backend.domain.careplan.payload.GoalDto
import org.hl7.fhir.r4.model.CarePlan
import org.hl7.fhir.r4.model.Reference

object CarePlanConverter {
    fun toFhir(patientId: Long, goals: List<GoalDto>): CarePlan {
        val carePlan = CarePlan()

        // Set ID
        carePlan.id = "careplan-$patientId"

        // Set status
        carePlan.status = CarePlan.CarePlanStatus.ACTIVE

        // Set intent
        carePlan.intent = CarePlan.CarePlanIntent.PLAN

        // Set subject (patient)
        carePlan.subject = Reference("Patient/$patientId")

        // Add goals
        goals.forEach { goalDto ->
            val goalReference = Reference("Goal/${goalDto.id}")
            goalReference.display = goalDto.name
            carePlan.addGoal(goalReference)
        }

        return carePlan
    }
}
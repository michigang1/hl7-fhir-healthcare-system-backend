package michigang1.healthcare.backend.domain.careplan.payload

import michigang1.healthcare.backend.domain.careplan.model.CarePlan
import michigang1.healthcare.backend.domain.careplan.model.Goal
import michigang1.healthcare.backend.domain.careplan.model.Measure
import michigang1.healthcare.backend.domain.patient.model.Patient

object CarePlanMapper {
    fun toResponse(carePlan: CarePlan): CarePlanResponse {
        return CarePlanResponse(
            id = carePlan.id,
            patientId = carePlan.patient.id,
            patientName = "${carePlan.patient.firstName} ${carePlan.patient.lastName}",
            measureId = carePlan.measure.id,
            measureName = carePlan.measure.name,
            measureDescription = carePlan.measure.description,
            goalId = carePlan.goal.id,
            goalName = carePlan.goal.name,
            goalDescription = carePlan.goal.description,
            goalDuration = carePlan.goal.duration,
            goalFrequency = carePlan.goal.frequency,
            scheduledDateTime = carePlan.scheduledDateTime,
            isCompleted = carePlan.isCompleted,
            isGoalAchieved = carePlan.isGoalAchieved,
            notes = carePlan.notes
        )
    }

    fun toEntity(request: CarePlanRequest, patient: Patient, measure: Measure, goal: Goal): CarePlan {
        return CarePlan(
            patient = patient,
            measure = measure,
            goal = goal,
            scheduledDateTime = request.scheduledDateTime ?: throw IllegalArgumentException("Scheduled date time is required"),
            isCompleted = request.isCompleted ?: false,
            isGoalAchieved = request.isGoalAchieved ?: false,
            notes = request.notes
        )
    }

    fun updateEntity(carePlan: CarePlan, request: CarePlanRequest, measure: Measure? = null, goal: Goal? = null): CarePlan {
        return CarePlan(
            id = carePlan.id,
            patient = carePlan.patient,
            measure = measure ?: carePlan.measure,
            goal = goal ?: carePlan.goal,
            scheduledDateTime = request.scheduledDateTime ?: carePlan.scheduledDateTime,
            isCompleted = request.isCompleted ?: carePlan.isCompleted,
            isGoalAchieved = request.isGoalAchieved ?: carePlan.isGoalAchieved,
            notes = request.notes ?: carePlan.notes
        )
    }
}
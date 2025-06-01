package michigang1.healthcare.backend.domain.careplan.payload

import michigang1.healthcare.backend.domain.careplan.model.Goal

object GoalMapper {
    fun toDto(goal: Goal): GoalDto =
        GoalDto(
            id = goal.id,
            name = goal.name,
            description = goal.description,
            patientId = goal.patient.id
        )
}


package michigang1.healthcare.backend.domain.careplan.payload

import michigang1.healthcare.backend.domain.careplan.model.Goal
import michigang1.healthcare.backend.domain.careplan.model.GoalTemplate

object GoalMapper {
    fun toResponse(goal: Goal): GoalResponse {
        return GoalResponse(
            id = goal.id,
            name = goal.name,
            description = goal.description,
            duration = goal.duration,
            frequency = goal.frequency,
            templateId = goal.template?.id,
            templateName = goal.template?.name
        )
    }

    fun toEntity(request: GoalRequest, template: GoalTemplate? = null): Goal {
        return Goal(
            name = request.name ?: throw IllegalArgumentException("Name is required"),
            description = request.description ?: throw IllegalArgumentException("Description is required"),
            duration = request.duration ?: throw IllegalArgumentException("Duration is required"),
            frequency = request.frequency ?: throw IllegalArgumentException("Frequency is required"),
            template = template
        )
    }

    fun updateEntity(goal: Goal, request: GoalRequest, template: GoalTemplate? = null): Goal {
        return Goal(
            id = goal.id,
            name = request.name ?: goal.name,
            description = request.description ?: goal.description,
            duration = request.duration ?: goal.duration,
            frequency = request.frequency ?: goal.frequency,
            template = template ?: goal.template
        )
    }
}
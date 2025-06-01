package michigang1.healthcare.backend.domain.careplan.payload

import michigang1.healthcare.backend.domain.careplan.model.Measure

object MeasureMapper {
    fun toDto(measure: Measure): MeasureDto =
        MeasureDto(
            id = measure.id,
            name = measure.name,
            description = measure.description,
            scheduledDateTime = measure.scheduledDateTime,
            isCompleted = measure.isCompleted,
            goalId = measure.goal.id,
            goalName = measure.goal.name
        )
}
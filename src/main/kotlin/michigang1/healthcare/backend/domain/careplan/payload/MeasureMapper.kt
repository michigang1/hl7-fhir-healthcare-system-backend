package michigang1.healthcare.backend.domain.careplan.payload

import michigang1.healthcare.backend.domain.careplan.model.Measure
import michigang1.healthcare.backend.domain.careplan.model.MeasureTemplate

object MeasureMapper {
    fun toResponse(measure: Measure): MeasureResponse {
        return MeasureResponse(
            id = measure.id,
            name = measure.name,
            description = measure.description,
            templateId = measure.template?.id,
            templateName = measure.template?.name
        )
    }

    fun toEntity(request: MeasureRequest, template: MeasureTemplate? = null): Measure {
        return Measure(
            name = request.name ?: throw IllegalArgumentException("Name is required"),
            description = request.description ?: throw IllegalArgumentException("Description is required"),
            template = template
        )
    }

    fun updateEntity(measure: Measure, request: MeasureRequest, template: MeasureTemplate? = null): Measure {
        return Measure(
            id = measure.id,
            name = request.name ?: measure.name,
            description = request.description ?: measure.description,
            template = template ?: measure.template
        )
    }
}
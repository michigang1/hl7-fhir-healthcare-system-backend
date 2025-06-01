package michigang1.healthcare.backend.domain.careplan.service

import michigang1.healthcare.backend.domain.careplan.payload.MeasureRequest
import michigang1.healthcare.backend.domain.careplan.payload.MeasureResponse
import reactor.core.publisher.Mono

interface MeasureService {
    fun getAll(): Mono<List<MeasureResponse>>
    fun getById(id: Long): Mono<MeasureResponse?>
    fun createMeasure(measureRequest: MeasureRequest): Mono<MeasureResponse>
    fun updateMeasure(id: Long, measureRequest: MeasureRequest): Mono<MeasureResponse?>
    fun deleteMeasure(id: Long): Boolean
}
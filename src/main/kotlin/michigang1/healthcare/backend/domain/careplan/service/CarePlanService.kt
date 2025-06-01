package michigang1.healthcare.backend.domain.careplan.service

import michigang1.healthcare.backend.domain.careplan.payload.CarePlanRequest
import michigang1.healthcare.backend.domain.careplan.payload.CarePlanResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CarePlanService {
    fun getAll(): Mono<List<CarePlanResponse>>
    fun getById(id: Long): Mono<CarePlanResponse?>
    fun getByPatientId(patientId: Long): Mono<List<CarePlanResponse>>
    fun createCarePlan(carePlanRequest: CarePlanRequest): Mono<CarePlanResponse>
    fun updateCarePlan(id: Long, carePlanRequest: CarePlanRequest): Mono<CarePlanResponse?>
    fun deleteCarePlan(id: Long): Boolean
}
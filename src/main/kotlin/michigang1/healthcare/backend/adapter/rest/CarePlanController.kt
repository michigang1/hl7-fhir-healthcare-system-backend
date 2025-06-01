package michigang1.healthcare.backend.adapter.rest

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.payload.CarePlanRequest
import michigang1.healthcare.backend.domain.careplan.payload.CarePlanResponse
import michigang1.healthcare.backend.domain.careplan.service.CarePlanService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@Validated
class CarePlanController(
    private val carePlanService: CarePlanService
) {
    // Endpoints for /api/v1/patients/{id}/careplans
    @GetMapping("/api/v1/patients/{patientId}/careplans")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getCarePlansByPatientId(@PathVariable patientId: Long): Mono<ResponseEntity<List<CarePlanResponse>>> =
        carePlanService.getByPatientId(patientId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/api/v1/patients/{patientId}/careplans")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE')")
    fun createCarePlan(
        @PathVariable patientId: Long,
        @RequestBody @Validated carePlanRequest: CarePlanRequest
    ): Mono<ResponseEntity<CarePlanResponse>> {
        // Ensure patientId from path is used
        val requestWithPatientId = carePlanRequest.copy(patientId = patientId)
        return carePlanService.createCarePlan(requestWithPatientId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())
    }

    // Endpoints for /api/v1/careplans/{id}
    @GetMapping("/api/v1/careplans/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getCarePlanById(@PathVariable id: Long): Mono<ResponseEntity<CarePlanResponse?>> =
        carePlanService.getById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/api/v1/careplans/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE')")
    fun updateCarePlan(
        @PathVariable id: Long,
        @RequestBody @Validated carePlanRequest: CarePlanRequest
    ): Mono<ResponseEntity<CarePlanResponse?>> =
        carePlanService.updateCarePlan(id, carePlanRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/api/v1/careplans/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun deleteCarePlan(@PathVariable id: Long): Mono<ResponseEntity<Boolean>> =
        Mono.fromCallable { carePlanService.deleteCarePlan(id) }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    // Additional endpoint to get all care plans
    @GetMapping("/api/v1/careplans")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllCarePlans(): Mono<ResponseEntity<List<CarePlanResponse>>> =
        carePlanService.getAll()
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
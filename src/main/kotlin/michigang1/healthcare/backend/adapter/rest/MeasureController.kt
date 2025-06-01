package michigang1.healthcare.backend.adapter.rest

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.payload.MeasureRequest
import michigang1.healthcare.backend.domain.careplan.payload.MeasureResponse
import michigang1.healthcare.backend.domain.careplan.service.MeasureService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@Validated
class MeasureController(
    private val measureService: MeasureService
) {
    // Endpoints for /careplans/measures
    @GetMapping("/careplans/measures")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllMeasures(): Mono<ResponseEntity<List<MeasureResponse>>> =
        measureService.getAll()
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/careplans/measures")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE')")
    fun createMeasure(
        @RequestBody @Validated measureRequest: MeasureRequest
    ): Mono<ResponseEntity<MeasureResponse>> =
        measureService.createMeasure(measureRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())

    // Endpoints for /careplans/measures/{id}
    @GetMapping("/careplans/measures/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getMeasureById(@PathVariable id: Long): Mono<ResponseEntity<MeasureResponse?>> =
        measureService.getById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/careplans/measures/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE')")
    fun updateMeasure(
        @PathVariable id: Long,
        @RequestBody @Validated measureRequest: MeasureRequest
    ): Mono<ResponseEntity<MeasureResponse?>> =
        measureService.updateMeasure(id, measureRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/careplans/measures/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun deleteMeasure(@PathVariable id: Long): Mono<ResponseEntity<Boolean>> =
        Mono.fromCallable { measureService.deleteMeasure(id) }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
package michigang1.healthcare.backend.adapter.rest

import jakarta.validation.Valid
import michigang1.healthcare.backend.domain.medications.payload.MedicationRequest
import michigang1.healthcare.backend.domain.medications.payload.MedicationResponse

import michigang1.healthcare.backend.domain.medications.service.MedicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@RestController
@RequestMapping("/api/v1")
class MedicationController(
    private val medicationService: MedicationService
) {
    @GetMapping("/medications")
    fun getAllMedications(): Mono<ResponseEntity<List<MedicationResponse>>> =
        medicationService.getAllMedications()
            .flatMapIterable { it }
            .collectList()
            .map { ResponseEntity.ok(it) }

    @GetMapping("/patients/{patientId}/medications")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER') or hasRole('ORGANISATION_ADMIN') or hasRole('ADMIN')")
    fun getAllByPatient(@PathVariable patientId: Long): Mono<ResponseEntity<List<MedicationResponse>>> =
        medicationService.getAllByPatient(patientId)
            .flatMapIterable { it }
            .collectList()
            .map { ResponseEntity.ok(it) }

    @GetMapping("/patients/{patientId}/medications/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER') or hasRole('ORGANISATION_ADMIN') or hasRole('ADMIN')")
    fun getById(
        @PathVariable patientId: Long,
        @PathVariable id: Long
    ): Mono<ResponseEntity<MedicationResponse>> =
        medicationService.getMedicationByPatient(patientId, id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/patients/{patientId}/medications")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE') or hasRole('ADMIN')")
    fun create(
        @PathVariable patientId: Long,
        @Valid @RequestBody request: MedicationRequest
    ): Mono<ResponseEntity<MedicationResponse>> {
        val req = request.copy(patientId = patientId)
        return medicationService.createMedication(req)
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @PutMapping("/patients/{patientId}/medications/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE') or hasRole('ADMIN')")
    fun update(
        @PathVariable patientId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody request: MedicationRequest
    ): Mono<ResponseEntity<MedicationResponse>> {
        val req = request.copy(patientId = patientId)
        return medicationService.updateMedication(patientId, id, req)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/patients/{patientId}/medications/{id}")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN') or hasRole('ADMIN')")
    fun delete(
        @PathVariable patientId: Long,
        @PathVariable id: Long
    ): Mono<ResponseEntity<Boolean>> =
        medicationService.deleteMedication(patientId, id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
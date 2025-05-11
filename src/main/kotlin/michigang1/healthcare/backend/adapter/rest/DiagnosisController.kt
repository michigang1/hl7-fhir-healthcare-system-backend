package michigang1.healthcare.backend.adapter.rest

import michigang1.healthcare.backend.domain.diagnoses.payload.request.DiagnosisRequest
import michigang1.healthcare.backend.domain.diagnoses.payload.response.DiagnosisResponse
import michigang1.healthcare.backend.domain.diagnoses.service.DiagnosisService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1")
@Validated
class DiagnosisController(private val diagnosisService: DiagnosisService) {


    @GetMapping("/diagnoses")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllDiagnoses(): Mono<ResponseEntity<List<DiagnosisResponse>>> =
        diagnosisService.getAllDiagnoses()
            .flatMapIterable { it }
            .collectList()
            .map { ResponseEntity.ok(it) }


    @GetMapping("/patients/{patientId}/diagnoses/{diagnosisId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getDiagnosisByPatient(@PathVariable patientId: Long, @PathVariable diagnosisId:Long): Mono<ResponseEntity<DiagnosisResponse>> =
        diagnosisService.getDiagnosisByPatient(patientId, diagnosisId)
            .map { ResponseEntity.ok(it) }

    @GetMapping("/patients/{patientId}/diagnoses")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllDiagnosesByPatient(@PathVariable patientId: Long): Mono<ResponseEntity<List<DiagnosisResponse>>> =
        diagnosisService.getAllDiagnosesByPatient(patientId)
            .flatMapIterable { it }
            .collectList()
            .map { ResponseEntity.ok(it) }

    @PostMapping("/diagnoses")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun createDiagnosis(
        @RequestBody @Validated request: DiagnosisRequest
    ): Mono<ResponseEntity<DiagnosisResponse>> =
        diagnosisService.createDiagnosis(request)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())

    @PutMapping("/patients/{patientId}/diagnoses/{diagnosisId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun updateDiagnosis(
        @RequestBody @Validated request: DiagnosisRequest,
        @PathVariable patientId: Long,
        @PathVariable diagnosisId: Long
    ): Mono<ResponseEntity<DiagnosisResponse>> =
        diagnosisService.updateDiagnosis(patientId, diagnosisId, request)
            .map { ResponseEntity.ok(it) }

    @DeleteMapping("/patients/{patientId}/diagnoses/{diagnosisId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun deleteDiagnosis(@PathVariable patientId: Long, @PathVariable diagnosisId: Long): Mono<ResponseEntity<Boolean>> =
        diagnosisService.deleteDiagnosis(patientId,diagnosisId)
            .map { ResponseEntity.ok(it) }
}
package michigang1.healthcare.backend.adapter.rest


import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.patient.payload.PatientRequest
import michigang1.healthcare.backend.domain.patient.payload.PatientResponse
import michigang1.healthcare.backend.domain.patient.service.PatientService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/patients")
@Validated
class PatientController(
    private val patientService: PatientService,
) {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or" +
                  " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
                  " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllPatients(): Flux<List<PatientResponse>> =
        patientService.getAll()

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getPatientById(@PathVariable id: Long): Mono<ResponseEntity<PatientResponse?>> =
        patientService.getById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun createPatient(@RequestBody @Validated patientRequest: PatientRequest): Mono<ResponseEntity<PatientResponse>> =
        patientService.createPatient(patientRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun updatePatient(
        @PathVariable id: Long,
        @RequestBody @Validated patientRequest: PatientRequest
    ): Mono<ResponseEntity<PatientResponse?>> =
        patientService.updatePatient(id, patientRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun deletePatient(@PathVariable id: Long): Mono<ResponseEntity<Boolean>> =
        Mono.fromCallable { patientService.deletePatient(id) }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
package michigang1.healthcare.backend.adapter.rest

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.payload.GoalDto
import michigang1.healthcare.backend.domain.careplan.payload.MeasureDto
import michigang1.healthcare.backend.domain.careplan.service.CarePlanService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/patients/{patientId}/careplan")
@Validated
class CarePlanController(
    private val carePlanService: CarePlanService
) {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getCarePlanByPatientId(@PathVariable patientId: Long): Mono<ResponseEntity<List<MeasureDto>>> =
        carePlanService.getCarePlanByPatientId(patientId)
            .map { ResponseEntity.ok(it) }

    @PostMapping("/goals")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun createGoal(@RequestBody goalDto: GoalDto): Mono<ResponseEntity<GoalDto>> =
        carePlanService.createGoal(goalDto)
            .map { ResponseEntity.ok(it) }

    @PostMapping("/goals/{goalId}/measures")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun createMeasure(
        @PathVariable goalId: Long,
        @RequestBody measureDto: MeasureDto
    ): Mono<ResponseEntity<MeasureDto>> =
        carePlanService.createMeasure(goalId, measureDto)
            .map { ResponseEntity.ok(it) }

    @GetMapping("/goals/{goalId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getGoalById(@PathVariable goalId: Long): Mono<ResponseEntity<GoalDto?>> =
        carePlanService.getGoalById(goalId)
            .map { ResponseEntity.ok(it) }

    @GetMapping("/goals/{goalId}/measures/{measureId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getMeasureById(
        @PathVariable goalId: Long,
        @PathVariable measureId: Long
    ): Mono<ResponseEntity<MeasureDto?>> =
        carePlanService.getMeasureById(goalId, measureId)
            .map { ResponseEntity.ok(it) }

    @PutMapping("/goals/{goalId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun updateGoal(@PathVariable goalId: Long, @RequestBody goalDto: GoalDto): Mono<ResponseEntity<GoalDto>> =
        carePlanService.updateGoal(goalId, goalDto)
            .map { ResponseEntity.ok(it) }

    @PutMapping("/goals/{goalId}/measures/{measureId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun updateMeasure(
        @PathVariable goalId: Long,
        @PathVariable measureId: Long,
        @RequestBody measureDto: MeasureDto
    ): Mono<ResponseEntity<MeasureDto>> =
        carePlanService.updateMeasure(goalId, measureId, measureDto)
            .map { ResponseEntity.ok(it) }

    @DeleteMapping("/goals/{goalId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun deleteGoal(@PathVariable goalId: Long): Mono<ResponseEntity<Unit>> =
        carePlanService.deleteGoal(goalId)
            .map { ResponseEntity.ok(it) }

    @DeleteMapping("/goals/{goalId}/measures/{measureId}")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE')")
    fun deleteMeasure(
        @PathVariable goalId: Long,
        @PathVariable measureId: Long
    ): Mono<ResponseEntity<Unit>> =
        carePlanService.deleteMeasure(goalId, measureId)
            .map { ResponseEntity.ok(it) }

    @GetMapping("/goals")
    @PreAuthorize("hasRole('ADMIN') or" +
            " hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or" +
            " hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllGoalsByPatient(@PathVariable patientId: Long): Mono<ResponseEntity<List<GoalDto>>> =
        carePlanService.getAllGoalsByPatient(patientId)
            .map { ResponseEntity.ok(it) }

}

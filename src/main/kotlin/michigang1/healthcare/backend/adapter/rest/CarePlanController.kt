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
    fun getCarePlanByPatientId(@PathVariable patientId: Long): ResponseEntity<Mono<List<MeasureDto>>> =
        ResponseEntity.ok(carePlanService.getCarePlanByPatientId(patientId))

    @PostMapping("/goals")
    fun createGoal(@RequestBody goalDto: GoalDto): ResponseEntity<Mono<GoalDto>> =
        ResponseEntity.ok(carePlanService.createGoal(goalDto))

    @PostMapping("/goals/{goalId}/measures")
    fun createMeasure(
        @PathVariable goalId: Long,
        @RequestBody measureDto: MeasureDto
    ): ResponseEntity<Mono<MeasureDto>> =
        ResponseEntity.ok(carePlanService.createMeasure(goalId, measureDto))

    @GetMapping("/goals/{goalId}")
    fun getGoalById(@PathVariable goalId: Long): ResponseEntity<Mono<GoalDto?>> =
        ResponseEntity.ok(carePlanService.getGoalById(goalId))

    @GetMapping("/goals/{goalId}/measures/{measureId}")
    fun getMeasureById(
        @PathVariable goalId: Long,
        @PathVariable measureId: Long
    ): ResponseEntity<Mono<MeasureDto?>> =
        ResponseEntity.ok(carePlanService.getMeasureById(goalId, measureId))

    @PutMapping("/goals/{goalId}")
    fun updateGoal(@PathVariable goalId: Long, @RequestBody goalDto: GoalDto): ResponseEntity<Mono<GoalDto>> =
        ResponseEntity.ok(carePlanService.updateGoal(goalId, goalDto))

    @PutMapping("/goals/{goalId}/measures/{measureId}")
    fun updateMeasure(
        @PathVariable goalId: Long,
        @PathVariable measureId: Long,
        @RequestBody measureDto: MeasureDto
    ): ResponseEntity<Mono<MeasureDto>> =
        ResponseEntity.ok(carePlanService.updateMeasure(goalId, measureId, measureDto))

    @DeleteMapping("/goals/{goalId}")
    fun deleteGoal(@PathVariable goalId: Long): ResponseEntity<Mono<Unit>> =
        ResponseEntity.ok(carePlanService.deleteGoal(goalId))

    @DeleteMapping("/goals/{goalId}/measures/{measureId}")
    fun deleteMeasure(
        @PathVariable goalId: Long,
        @PathVariable measureId: Long
    ): ResponseEntity<Mono<Unit>> =
        ResponseEntity.ok(carePlanService.deleteMeasure(goalId, measureId))

    @GetMapping("/goals")
    fun getAllGoalsByPatient(@PathVariable patientId: Long): ResponseEntity<Mono<List<GoalDto>>> =
        ResponseEntity.ok(carePlanService.getAllGoalsByPatient(patientId))
}

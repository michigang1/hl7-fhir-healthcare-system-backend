package michigang1.healthcare.backend.adapter.rest

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.payload.GoalRequest
import michigang1.healthcare.backend.domain.careplan.payload.GoalResponse
import michigang1.healthcare.backend.domain.careplan.service.GoalService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@Validated
class GoalController(
    private val goalService: GoalService
) {
    // Endpoints for /careplans/goals
    @GetMapping("/careplans/goals")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getAllGoals(): Mono<ResponseEntity<List<GoalResponse>>> =
        goalService.getAll()
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/careplans/goals")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE')")
    fun createGoal(
        @RequestBody @Validated goalRequest: GoalRequest
    ): Mono<ResponseEntity<GoalResponse>> =
        goalService.createGoal(goalRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())

    // Endpoints for /careplans/goals/{id}
    @GetMapping("/careplans/goals/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE') or hasRole('SOCIAL_WORKER')")
    fun getGoalById(@PathVariable id: Long): Mono<ResponseEntity<GoalResponse?>> =
        goalService.getById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/careplans/goals/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR') or hasRole('NURSE')")
    fun updateGoal(
        @PathVariable id: Long,
        @RequestBody @Validated goalRequest: GoalRequest
    ): Mono<ResponseEntity<GoalResponse?>> =
        goalService.updateGoal(id, goalRequest)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/careplans/goals/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISATION_ADMIN') or hasRole('DOCTOR')")
    fun deleteGoal(@PathVariable id: Long): Mono<ResponseEntity<Boolean>> =
        Mono.fromCallable { goalService.deleteGoal(id) }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
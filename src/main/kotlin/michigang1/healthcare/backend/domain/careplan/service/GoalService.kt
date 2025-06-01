package michigang1.healthcare.backend.domain.careplan.service

import michigang1.healthcare.backend.domain.careplan.payload.GoalRequest
import michigang1.healthcare.backend.domain.careplan.payload.GoalResponse
import reactor.core.publisher.Mono

interface GoalService {
    fun getAll(): Mono<List<GoalResponse>>
    fun getById(id: Long): Mono<GoalResponse?>
    fun createGoal(goalRequest: GoalRequest): Mono<GoalResponse>
    fun updateGoal(id: Long, goalRequest: GoalRequest): Mono<GoalResponse?>
    fun deleteGoal(id: Long): Boolean
}
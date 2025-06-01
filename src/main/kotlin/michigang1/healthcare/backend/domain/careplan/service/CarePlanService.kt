package michigang1.healthcare.backend.domain.careplan.service

import michigang1.healthcare.backend.domain.careplan.payload.GoalDto
import michigang1.healthcare.backend.domain.careplan.payload.MeasureDto
import reactor.core.publisher.Mono

interface CarePlanService {
    fun getCarePlanByPatientId(patientId: Long): Mono<List<MeasureDto>>
    fun createGoal(goalDto: GoalDto): Mono<GoalDto>
    fun createMeasure(goalId: Long, dto: MeasureDto): Mono<MeasureDto>
    fun deleteGoal(goalId: Long): Mono<Unit>
    fun deleteMeasure(goalId:Long, measureId: Long): Mono<Unit>
    fun updateGoal(goalId: Long, goalDto: GoalDto): Mono<GoalDto>
    fun updateMeasure(goalId:Long, measureId: Long, measureDto: MeasureDto): Mono<MeasureDto>
    fun getGoalById(goalId: Long): Mono<GoalDto?>
    fun getMeasureById(goalId:Long, measureId: Long): Mono<MeasureDto?>
    fun getAllGoalsByPatient(patientId: Long): Mono<List<GoalDto>>

}
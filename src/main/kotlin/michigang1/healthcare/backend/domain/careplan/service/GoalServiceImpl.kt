package michigang1.healthcare.backend.domain.careplan.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.payload.GoalMapper
import michigang1.healthcare.backend.domain.careplan.payload.GoalRequest
import michigang1.healthcare.backend.domain.careplan.payload.GoalResponse
import michigang1.healthcare.backend.domain.careplan.repository.GoalRepository
import michigang1.healthcare.backend.domain.careplan.repository.GoalTemplateRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.NoSuchElementException

@Service
class GoalServiceImpl(
    private val goalRepository: GoalRepository,
    private val goalTemplateRepository: GoalTemplateRepository
) : GoalService {

    override fun getAll(): Mono<List<GoalResponse>> {
        return Mono.fromCallable {
            goalRepository.findAll().map { GoalMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getById(id: Long): Mono<GoalResponse?> {
        return Mono.fromCallable {
            val goal = goalRepository.findById(id).orElse(null)
            goal?.let { GoalMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun createGoal(goalRequest: GoalRequest): Mono<GoalResponse> {
        return Mono.fromCallable {
            // Get template if provided
            val template = goalRequest.templateId?.let {
                goalTemplateRepository.findById(it)
                    .orElseThrow { NoSuchElementException("Goal template not found") }!!
            }

            // Create goal
            val goal = GoalMapper.toEntity(goalRequest, template)
            val savedGoal = goalRepository.save(goal)

            GoalMapper.toResponse(savedGoal)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun updateGoal(id: Long, goalRequest: GoalRequest): Mono<GoalResponse?> {
        return Mono.fromCallable {
            val existingGoal = goalRepository.findById(id)
                .orElseThrow { NoSuchElementException("Goal not found") }!!

            // Get template if provided
            val template = goalRequest.templateId?.let {
                goalTemplateRepository.findById(it)
                    .orElseThrow { NoSuchElementException("Goal template not found") }!!
            }

            // Update goal
            val updatedGoal = GoalMapper.updateEntity(existingGoal, goalRequest, template)
            val savedGoal = goalRepository.save(updatedGoal)

            GoalMapper.toResponse(savedGoal)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun deleteGoal(id: Long): Boolean {
        val goal = goalRepository.findById(id).orElseThrow {
            NoSuchElementException("Goal with ID $id not found")
        }!!

        goalRepository.delete(goal)

        return !goalRepository.existsById(id)
    }
}
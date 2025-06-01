package michigang1.healthcare.backend.domain.careplan.repository

import michigang1.healthcare.backend.domain.careplan.model.GoalTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalTemplateRepository : JpaRepository<GoalTemplate, Long>
package michigang1.healthcare.backend.common.util.fhir

import michigang1.healthcare.backend.domain.careplan.payload.GoalDto
import michigang1.healthcare.backend.domain.careplan.repository.MeasureRepository
import org.hl7.fhir.r4.model.*
import java.time.ZoneId
import java.util.*

class GoalConverter(private val measureRepository: MeasureRepository) {
    fun toFhir(goalDto: GoalDto): Goal {
        val goal = Goal()

        // Set ID
        goal.id = goalDto.id.toString()

        // Set identifier
        val identifier = Identifier()
        identifier.system = "urn:system:goal"
        identifier.value = goalDto.id.toString()
        goal.addIdentifier(identifier)

        // Set lifecycle status
        goal.lifecycleStatus = Goal.GoalLifecycleStatus.ACTIVE

        // Set achievement status
        val achievementStatus = CodeableConcept()
        achievementStatus.text = "in-progress"
        goal.achievementStatus = achievementStatus

        // Set category
        val category = CodeableConcept()
        category.text = "Treatment"
        goal.addCategory(category)

        // Set priority
        val priority = CodeableConcept()
        priority.text = "medium-priority"
        goal.priority = priority

        // Set description
        val description = CodeableConcept()
        description.text = goalDto.name
        goal.description = description

        // Set subject (patient)
        goal.subject = Reference("Patient/${goalDto.patientId}")

        // Set start date (using current date as default)
        val startDate = DateType()
        startDate.value = Date()
        goal.start = startDate

        // Set target
        val target = Goal.GoalTargetComponent()
        val dueDate = DateType()
        dueDate.value = Date() // Using current date as default
        target.due = dueDate
        goal.addTarget(target)

        // Set status date
        goal.statusDate = Date()

        // Set addresses
        goal.addAddresses(Reference("Patient/${goalDto.patientId}"))

        // Set note
        val note = Annotation()
        note.text = goalDto.description
        goal.addNote(note)

        // Add measures as contained resources
        val measures = measureRepository.findByGoalId(goalDto.id)
        measures.forEach { measure ->
            val observation = Observation()
            observation.id = measure.id.toString()
            observation.status = Observation.ObservationStatus.REGISTERED

            // Set code (type of measure)
            val codeableConcept = CodeableConcept()
            codeableConcept.text = measure.name
            observation.code = codeableConcept

            // Set subject (patient)
            observation.subject = Reference("Patient/${goalDto.patientId}")

            // Set focus (goal)
            observation.addFocus(Reference("Goal/${goalDto.id}"))

            // Set note
            val measureNote = Annotation()
            measureNote.text = measure.description
            observation.addNote(measureNote)

            // Set scheduled date
            val effectiveDateTime = DateTimeType()
            effectiveDateTime.value = DateConverter.convertLocalDateTimeToDate(measure.scheduledDateTime)
            observation.effective = effectiveDateTime

            // Set status (completed or not)
            if (measure.isCompleted) {
                val valueCodeableConcept = CodeableConcept()
                valueCodeableConcept.text = "Completed"
                observation.setValue(valueCodeableConcept)
            } else {
                val valueCodeableConcept = CodeableConcept()
                valueCodeableConcept.text = "In Progress"
                observation.setValue(valueCodeableConcept)
            }

            // Add observation as contained resource
            goal.addContained(observation)
        }

        return goal
    }

    fun fromFhir(fhirGoal: Goal): GoalDto {
        // Extract data from FHIR Goal
        val id = if (fhirGoal.hasId()) fhirGoal.idElement.idPart.toLongOrNull() ?: 0L else 0L

        // Extract patient reference
        val patientId = fhirGoal.subject.reference
            .replace("Patient/", "")
            .toLongOrNull() ?: throw IllegalArgumentException("Invalid patient reference")

        // Extract name (description)
        val name = fhirGoal.description.text ?: ""

        // Extract description (note)
        val description = fhirGoal.note.firstOrNull()?.text ?: ""

        // Create and return GoalDto
        return GoalDto(
            id = id,
            name = name,
            description = description,
            patientId = patientId
        )
    }
}
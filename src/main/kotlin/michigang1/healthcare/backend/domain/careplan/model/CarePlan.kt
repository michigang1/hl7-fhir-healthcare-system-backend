package michigang1.healthcare.backend.domain.careplan.model

import jakarta.persistence.*
import michigang1.healthcare.backend.domain.patient.model.Patient
import java.time.LocalDateTime

@Entity
@Table(name = "care_plans")
data class CarePlan(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: Patient,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measure_id", nullable = false)
    val measure: Measure,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    val goal: Goal,

    @Column(nullable = false)
    val scheduledDateTime: LocalDateTime,

    @Column(nullable = false)
    val isCompleted: Boolean = false,

    @Column(nullable = false)
    val isGoalAchieved: Boolean = false,

    @Column(nullable = true)
    val notes: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CarePlan

        if (id != other.id) return false
        if (scheduledDateTime != other.scheduledDateTime) return false
        if (isCompleted != other.isCompleted) return false
        if (isGoalAchieved != other.isGoalAchieved) return false
        if (notes != other.notes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + scheduledDateTime.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + isGoalAchieved.hashCode()
        result = 31 * result + (notes?.hashCode() ?: 0)
        return result
    }
}
package michigang1.healthcare.backend.domain.careplan.model

import jakarta.persistence.*

@Entity
@Table(name = "goals")
data class Goal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val duration: String,

    @Column(nullable = false)
    val frequency: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = true)
    val template: GoalTemplate? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Goal

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (duration != other.duration) return false
        if (frequency != other.frequency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + frequency.hashCode()
        return result
    }
}
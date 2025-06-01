package michigang1.healthcare.backend.domain.careplan.model

import jakarta.persistence.*
import java.time.LocalDateTime

// Patient — предполагается, что у тебя уже есть эта сущность


@Entity
@Table(name = "measures")
data class Measure(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val scheduledDateTime: LocalDateTime,

    @Column(nullable = false)
    val isCompleted: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    val goal: Goal
)

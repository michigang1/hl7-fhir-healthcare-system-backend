package michigang1.healthcare.backend.domain.careplan.model

import jakarta.persistence.*
import michigang1.healthcare.backend.domain.patient.model.Patient

@Entity
@Table(name = "goals")
data class Goal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: Patient
)

package michigang1.healthcare.backend.domain.diagnoses.model


import jakarta.persistence.*
import michigang1.healthcare.backend.domain.patient.model.Patient
import java.time.LocalDate

@Entity
@Table(name = "patient_diagnoses")
data class Diagnosis(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: Patient,

    @Column(nullable = false, length = 20)
    val code: String,

    @Column(nullable = false)
    val isPrimary: Boolean = false,

    @Column(nullable = false, length = 255)
    val description: String,

    @Column(nullable = false)
    val diagnosedAt: LocalDate = LocalDate.now(),

    @Column(nullable = false, length = 30)
    val diagnosedBy: String
)

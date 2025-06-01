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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Diagnosis

        if (id != other.id) return false
        if (code != other.code) return false
        if (isPrimary != other.isPrimary) return false
        if (description != other.description) return false
        if (diagnosedAt != other.diagnosedAt) return false
        if (diagnosedBy != other.diagnosedBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + isPrimary.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + diagnosedAt.hashCode()
        result = 31 * result + diagnosedBy.hashCode()
        return result
    }
}

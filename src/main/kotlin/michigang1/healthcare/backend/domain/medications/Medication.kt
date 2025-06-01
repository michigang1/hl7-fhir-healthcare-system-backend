package michigang1.healthcare.backend.domain.medications

import jakarta.persistence.*
import michigang1.healthcare.backend.domain.patient.model.Patient
import java.time.LocalDate

@Entity
@Table(name = "medications")
data class Medication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: Patient,

    @Column(name = "medication_name", nullable = false)
    val medicationName: String,

    @Column(name = "dosage", nullable = false)
    val dosage: String,

    @Column(name = "frequency", nullable = false)
    val frequency: String,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date", nullable = true)
    val endDate: LocalDate? = null,

    @Column(name = "prescribed_by", nullable = false)
    val prescribedBy: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Medication

        if (id != other.id) return false
        if (medicationName != other.medicationName) return false
        if (dosage != other.dosage) return false
        if (frequency != other.frequency) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (prescribedBy != other.prescribedBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + medicationName.hashCode()
        result = 31 * result + dosage.hashCode()
        result = 31 * result + frequency.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + (endDate?.hashCode() ?: 0)
        result = 31 * result + prescribedBy.hashCode()
        return result
    }
}

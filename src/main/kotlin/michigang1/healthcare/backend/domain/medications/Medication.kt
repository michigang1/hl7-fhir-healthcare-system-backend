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
)
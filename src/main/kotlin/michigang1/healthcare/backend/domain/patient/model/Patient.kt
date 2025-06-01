package michigang1.healthcare.backend.domain.patient.model

import jakarta.persistence.*
import michigang1.healthcare.backend.domain.careplan.model.CarePlan
import michigang1.healthcare.backend.domain.diagnoses.model.Diagnosis
import michigang1.healthcare.backend.domain.medications.Medication


@Entity
@Table(name = "patients")
data class Patient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val firstName: String,

    @Column(nullable = false)
    val lastName: String,

    @Column(nullable = false)
    val roomNo: String,

    @Column(nullable = false)
    val birthDate: String,

    /** "male", "female", "other", "unknown" */
    @Column(nullable = false, length = 20)
    val gender: String,

    @Column(nullable = false)
    val address: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val phone: String,

    /** например номер полиса или внутренний ID */
    @Column(nullable = false, unique = true)
    val identifier: Long,

    /** Ссылка на организацию, к которой привязан пациент */
    @Column(name = "organization_id")
    val organizationId: Long,

    @OneToMany(
        mappedBy = "patient",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val diagnoses: MutableSet<Diagnosis> = mutableSetOf(),

    @OneToMany(
        mappedBy = "patient",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val medications: MutableSet<Medication> = mutableSetOf(),

    @OneToMany(
        mappedBy = "patient",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val carePlans: MutableSet<CarePlan> = mutableSetOf()
    )

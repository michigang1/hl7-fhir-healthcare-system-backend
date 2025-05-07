package michigang1.healthcare.backend.domain.patient.model

import jakarta.persistence.*


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
)
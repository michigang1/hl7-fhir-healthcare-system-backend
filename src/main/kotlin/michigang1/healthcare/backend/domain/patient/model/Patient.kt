package michigang1.healthcare.backend.domain.patient.model

import jakarta.persistence.*
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
    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Patient

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (roomNo != other.roomNo) return false
        if (birthDate != other.birthDate) return false
        if (gender != other.gender) return false
        if (address != other.address) return false
        if (email != other.email) return false
        if (phone != other.phone) return false
        if (identifier != other.identifier) return false
        if (organizationId != other.organizationId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + roomNo.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + identifier.hashCode()
        result = 31 * result + organizationId.hashCode()
        return result
    }
}

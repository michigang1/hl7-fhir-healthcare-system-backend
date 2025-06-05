package michigang1.healthcare.backend.domain.auth.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    val name: RoleEnum
)

enum class RoleEnum {
    ROLE_PATIENT,
    ROLE_DOCTOR,
    ROLE_ADMIN,
    ROLE_ORGANISATION_ADMIN,
    ROLE_SOCIAL_WORKER,
    ROLE_NURSE,
    ROLE_PHARMACIST,
    ROLE_RECEPTIONIST
}
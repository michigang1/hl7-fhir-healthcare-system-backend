package michigang1.healthcare.backend.domain.organization.payload

import jakarta.validation.constraints.Pattern

data class OrganizationRequest(
    val id: Long?,

    @field:Pattern(
        regexp = "^[a-zA-Zа-яА-ЯёЁіІїЇєЄґҐ'\\s-0-9]+$",
        message = "Name must contain only letters, numbers, spaces, and hyphens"
    )
    val name: String?

)

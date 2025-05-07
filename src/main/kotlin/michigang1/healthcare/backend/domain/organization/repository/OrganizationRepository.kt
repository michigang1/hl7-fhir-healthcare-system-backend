package michigang1.healthcare.backend.domain.organization.repository

import michigang1.healthcare.backend.domain.organization.Organization
import org.springframework.data.jpa.repository.JpaRepository

interface OrganizationRepository: JpaRepository<Organization, Long> {
    fun findByName(name: String): Organization?

}
package michigang1.healthcare.backend.domain.auth.repository

import michigang1.healthcare.backend.domain.auth.model.Role
import michigang1.healthcare.backend.domain.auth.model.RoleEnum
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: RoleEnum): Role?
    fun findAllByNameIn(names: Collection<RoleEnum>): List<Role>
}
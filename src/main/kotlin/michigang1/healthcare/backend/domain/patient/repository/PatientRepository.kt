package michigang1.healthcare.backend.domain.patient.repository

import michigang1.healthcare.backend.domain.patient.model.Patient
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PatientRepository: JpaRepository<Patient?, Long> {
    override fun findAll(): List<Patient>

    override fun findById(id: Long): Optional<Patient?>
    override fun existsById(id: Long): Boolean

    fun findByFirstName(firstName:String): Patient?
    fun existsByFirstName(firstName: String): Boolean

    fun findByLastName(lastName: String): Patient?
    fun existsByLastName(lastName: String): Boolean

    fun findByEmail(email: String): Patient?
    fun existsByEmail(email: String): Boolean

    fun findByPhone(phone: String): Patient?
    fun existsByPhone(phone: String): Boolean

    fun findByAddress(address: String): List<Patient>
    fun existsByAddress(address: String): Boolean

    fun findByBirthDate(birthDate: String): List<Patient>
    fun existsByBirthDate(birthDate: String): Boolean

    fun findByIdentifier(identifier: Long): Patient?
    fun existsByIdentifier(identifier: Long): Boolean

    fun findByOrganizationId(organizationId: Long): List<Patient>
    fun existsByOrganizationId(organizationId: Long): Boolean


    fun save(patient: Patient): Patient

    override fun deleteById(id: Long)


}
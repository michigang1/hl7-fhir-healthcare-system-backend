package michigang1.healthcare.backend.domain.patient.service

import michigang1.healthcare.backend.domain.patient.model.Patient
import michigang1.healthcare.backend.domain.patient.payload.PatientRequest
import michigang1.healthcare.backend.domain.patient.payload.PatientResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PatientService {
    fun getAll(): Flux<List<PatientResponse>>
    fun getById(id: Long): Mono<PatientResponse?>
    fun getByFirstName(name: String): Mono<PatientResponse?>
    fun getByLastName(surname: String): Mono<PatientResponse?>
    fun getByEmail(email: String): Mono<PatientResponse?>
    fun getByPhone(phone: String): Mono<PatientResponse?>
    fun getByAddress(address: String): Flux<List<PatientResponse>>
    fun getByIdentifier(identifier: Long): Mono<PatientResponse>
    fun getByOrganizationId(organizationId: Long): Flux<List<PatientResponse>>

    fun createPatient(patient: PatientRequest): Mono<PatientResponse>

    fun updatePatient(id: Long, patient: PatientRequest): Mono<PatientResponse?>

    fun deletePatient(id: Long): Boolean
}
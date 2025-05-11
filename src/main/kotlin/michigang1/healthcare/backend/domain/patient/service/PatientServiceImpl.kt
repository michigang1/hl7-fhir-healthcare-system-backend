package michigang1.healthcare.backend.domain.patient.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.diagnoses.service.DiagnosisService
import michigang1.healthcare.backend.domain.organization.service.OrganizationService
import michigang1.healthcare.backend.domain.patient.payload.PatientRequest
import michigang1.healthcare.backend.domain.patient.payload.PatientResponse
import michigang1.healthcare.backend.domain.patient.repository.PatientRepository
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import java.text.DateFormat

@Service
class PatientServiceImpl(
    private val patientRepository: PatientRepository,
    private val organizationService: OrganizationService,
): PatientService {
    override fun getAll(): Flux<List<PatientResponse>> {
        return Mono.fromCallable { patientRepository.findAll() }
            .subscribeOn(Schedulers.boundedElastic())
            .map { patients ->
                patients.map { patient ->
                    PatientResponse(
                        id = patient.id,
                        name = patient.firstName,
                        surname = patient.lastName,
                        dateOfBirth = patient.birthDate.toString(),
                        gender = patient.gender,
                        address = patient.address,
                        email = patient.email,
                        phone = patient.phone,
                        identifier = patient.identifier,
                        organizationId = patient.organizationId,
                    )
                }
            }
            .flux()
    }
    override fun getById(id: Long): Mono<PatientResponse?> {
        return Mono.fromCallable {
            val patient = patientRepository.findById(id).orElseThrow {
                NoSuchElementException("Patient with ID $id not found")
            }
            PatientResponse(
                id = patient?.id,
                name = patient?.firstName,
                surname = patient?.lastName,
                dateOfBirth = patient?.birthDate.toString(),
                gender = patient?.gender,
                address = patient?.address,
                email = patient?.email,
                phone = patient?.phone,
                identifier = patient?.identifier,
                organizationId = patient?.organizationId,
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByFirstName(name: String): Mono<PatientResponse?> {
        return Mono.fromCallable {
            val patient = patientRepository.findByFirstName(name)
                ?: throw NoSuchElementException("There are no patients by this first name")
            PatientResponse(
                id = patient.id,
                name = patient.firstName,
                surname = patient.lastName,
                dateOfBirth = patient.birthDate.toString(),
                gender = patient.gender,
                address = patient.address,
                email = patient.email,
                phone = patient.phone,
                identifier = patient.identifier,
                organizationId = patient.organizationId,
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByLastName(surname: String): Mono<PatientResponse?> {
        return Mono.fromCallable {
            val patient = patientRepository.findByLastName(surname)
                ?: throw NoSuchElementException("There are no patients by this last name")
            PatientResponse(
                id = patient.id,
                name = patient.firstName,
                surname = patient.lastName,
                dateOfBirth = patient.birthDate.toString(),
                gender = patient.gender,
                address = patient.address,
                email = patient.email,
                phone = patient.phone,
                identifier = patient.identifier,
                organizationId = patient.organizationId,
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByEmail(email: String): Mono<PatientResponse?> {
        return Mono.fromCallable {
            val patient = patientRepository.findByEmail(email)
                ?: throw NoSuchElementException("There are no patients by this email")
            PatientResponse(
                id = patient.id,
                name = patient.firstName,
                surname = patient.lastName,
                dateOfBirth = patient.birthDate.toString(),
                gender = patient.gender,
                address = patient.address,
                email = patient.email,
                phone = patient.phone,
                identifier = patient.identifier,
                organizationId = patient.organizationId,
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByPhone(phone: String): Mono<PatientResponse?> {
        return Mono.fromCallable {
            val patient = patientRepository.findByPhone(phone)
                ?: throw NoSuchElementException("There are no patients by this phone")
            PatientResponse(
                id = patient.id,
                name = patient.firstName,
                surname = patient.lastName,
                dateOfBirth = patient.birthDate.toString(),
                gender = patient.gender,
                address = patient.address,
                email = patient.email,
                phone = patient.phone,
                identifier = patient.identifier,
                organizationId = patient.organizationId,
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByAddress(address: String): Flux<List<PatientResponse>> {
        return Mono.fromCallable { patientRepository.findByAddress(address) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { patients ->
                patients.map { patient ->
                    PatientResponse(
                        id = patient.id,
                        name = patient.firstName,
                        surname = patient.lastName,
                        dateOfBirth = patient.birthDate.toString(),
                        gender = patient.gender,
                        address = patient.address,
                        email = patient.email,
                        phone = patient.phone,
                        identifier = patient.identifier,
                        organizationId = patient.organizationId,
                    )
                }
            }
            .flux()
    }

    override fun getByIdentifier(identifier: Long): Mono<PatientResponse> {
        return Mono.fromCallable {
            val patient = patientRepository.findByIdentifier(identifier)
                ?: throw NoSuchElementException("There are no patients by this identifier")
            PatientResponse(
                id = patient.id,
                name = patient.firstName,
                surname = patient.lastName,
                dateOfBirth = patient.birthDate.toString(),
                gender = patient.gender,
                address = patient.address,
                email = patient.email,
                phone = patient.phone,
                identifier = patient.identifier,
                organizationId = patient.organizationId,
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getByOrganizationId(organizationId: Long): Flux<List<PatientResponse>> {
        return Mono.fromCallable { patientRepository.findByOrganizationId(organizationId) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { patients ->
                patients.map { patient ->
                    PatientResponse(
                        id = patient.id,
                        name = patient.firstName,
                        surname = patient.lastName,
                        dateOfBirth = patient.birthDate.toString(),
                        gender = patient.gender,
                        address = patient.address,
                        email = patient.email,
                        phone = patient.phone,
                        identifier = patient.identifier,
                        organizationId = patient.organizationId,
                    )
                }
            }
            .flux()
    }

    @Transactional
    override fun createPatient(patient: PatientRequest): Mono<PatientResponse> {
        return organizationService.getById(patient.organizationId ?: throw NoSuchElementException("Organization ID is required"))
            .switchIfEmpty(Mono.error(NoSuchElementException("Organization not found")))
            .flatMap { organization ->
                Mono.fromCallable {
                    val newPatient = michigang1.healthcare.backend.domain.patient.model.Patient(
                        firstName = patient.name ?: throw NoSuchElementException("First name is required"),
                        lastName = patient.surname ?: throw NoSuchElementException("Last name is required"),
                        birthDate = patient.dateOfBirth ?: throw NoSuchElementException("Date of birth is required"),
                        gender = patient.gender ?: throw NoSuchElementException("Gender is required"),
                        address = patient.address ?: throw NoSuchElementException("Address is required"),
                        email = patient.email ?: throw NoSuchElementException("Email is required"),
                        phone = patient.phone ?: throw NoSuchElementException("Phone is required"),
                        identifier = patient.identifier ?: throw NoSuchElementException("Identifier is required"),
                        organizationId = organization?.id ?: throw NoSuchElementException("Organization ID is required")
                    )

                    val savedPatient = patientRepository.save(newPatient)
                    PatientResponse(
                        id = savedPatient.id,
                        name = savedPatient.firstName,
                        surname = savedPatient.lastName,
                        dateOfBirth = savedPatient.birthDate.toString(),
                        gender = savedPatient.gender,
                        address = savedPatient.address,
                        email = savedPatient.email,
                        phone = savedPatient.phone,
                        identifier = savedPatient.identifier,
                        organizationId = savedPatient.organizationId
                    )
                }.subscribeOn(Schedulers.boundedElastic())
            }
    }

    @Transactional
    override fun updatePatient(id: Long, patient: PatientRequest): Mono<PatientResponse?> {
        return Mono.fromCallable {
            val existingPatient = patientRepository.findById(id).orElseThrow()

            val updatedPatient = existingPatient?.copy(
                firstName = patient.name ?: existingPatient.firstName,
                lastName = patient.surname ?: existingPatient.lastName,
                birthDate = patient.dateOfBirth ?: existingPatient.birthDate,
                gender = patient.gender ?: existingPatient.gender,
                address = patient.address ?: existingPatient.address,
                email = patient.email ?: existingPatient.email,
                phone = patient.phone ?: existingPatient.phone,
                identifier = patient.identifier ?: existingPatient.identifier,
                organizationId = patient.organizationId ?: existingPatient.organizationId
            )

            val savedPatient = patientRepository.save(updatedPatient!!)
            PatientResponse(
                id = savedPatient.id,
                name = savedPatient.firstName,
                surname = savedPatient.lastName,
                dateOfBirth = savedPatient.birthDate.toString(),
                gender = savedPatient.gender,
                address = savedPatient.address,
                email = savedPatient.email,
                phone = savedPatient.phone,
                identifier = savedPatient.identifier,
                organizationId = savedPatient.organizationId
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun deletePatient(id: Long): Boolean {
        val patient = patientRepository.findById(id).orElseThrow {
            NoSuchElementException("Patient with ID $id not found")
        }

        patientRepository.delete(patient!!)

        return !patientRepository.existsById(id)
    }

}
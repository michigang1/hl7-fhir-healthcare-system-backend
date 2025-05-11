package michigang1.healthcare.backend.domain.patient.payload


import michigang1.healthcare.backend.domain.patient.model.Patient

import org.springframework.stereotype.Component

@Component
class PatientMapper {

    fun toResponse(entity: Patient): PatientResponse =
        PatientResponse(
            id             = entity.id,
            name           = entity.firstName,
            surname        = entity.lastName,
            dateOfBirth    = entity.birthDate,
            gender         = entity.gender,
            address        = entity.address,
            email          = entity.email,
            phone          = entity.phone,
            identifier     = entity.identifier,
            organizationId = entity.organizationId
        )

    fun toEntity(request: PatientResponse): Patient =
        Patient(
            id             = request.id!!,
            firstName      = request.name!!,
            lastName       = request.surname!!,
            birthDate      = request.dateOfBirth!!,
            gender         = request.gender!!,
            address        = request.address!!,
            email          = request.email!!,
            phone          = request.phone!!,
            identifier     = request.identifier!!,
            organizationId = request.organizationId!!
        )
}
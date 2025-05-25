package michigang1.healthcare.backend.domain.medications.service


import michigang1.healthcare.backend.domain.medications.payload.MedicationRequest
import michigang1.healthcare.backend.domain.medications.payload.MedicationResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MedicationService {
    fun getAllMedications(): Flux<List<MedicationResponse>>
    fun getAllByPatient(patientId: Long): Flux<List<MedicationResponse>>
    fun getMedicationByPatient(patientId: Long, id: Long): Mono<MedicationResponse>
    fun createMedication(request: MedicationRequest): Mono<MedicationResponse>
    fun updateMedication(patientId: Long, id: Long, request: MedicationRequest): Mono<MedicationResponse>
    fun deleteMedication(patientId: Long, id: Long): Mono<Boolean>
}
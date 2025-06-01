package michigang1.healthcare.backend.domain.careplan.service

import jakarta.transaction.Transactional
import michigang1.healthcare.backend.domain.careplan.payload.MeasureMapper
import michigang1.healthcare.backend.domain.careplan.payload.MeasureRequest
import michigang1.healthcare.backend.domain.careplan.payload.MeasureResponse
import michigang1.healthcare.backend.domain.careplan.repository.MeasureRepository
import michigang1.healthcare.backend.domain.careplan.repository.MeasureTemplateRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.NoSuchElementException

@Service
class MeasureServiceImpl(
    private val measureRepository: MeasureRepository,
    private val measureTemplateRepository: MeasureTemplateRepository
) : MeasureService {

    override fun getAll(): Mono<List<MeasureResponse>> {
        return Mono.fromCallable {
            measureRepository.findAll().map { MeasureMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    override fun getById(id: Long): Mono<MeasureResponse?> {
        return Mono.fromCallable {
            val measure = measureRepository.findById(id).orElse(null)
            measure?.let { MeasureMapper.toResponse(it) }
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun createMeasure(measureRequest: MeasureRequest): Mono<MeasureResponse> {
        return Mono.fromCallable {
            // Get template if provided
            val template = measureRequest.templateId?.let {
                measureTemplateRepository.findById(it)
                    .orElseThrow { NoSuchElementException("Measure template not found") }!!
            }

            // Create measure
            val measure = MeasureMapper.toEntity(measureRequest, template)
            val savedMeasure = measureRepository.save(measure)

            MeasureMapper.toResponse(savedMeasure)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun updateMeasure(id: Long, measureRequest: MeasureRequest): Mono<MeasureResponse?> {
        return Mono.fromCallable {
            val existingMeasure = measureRepository.findById(id)
                .orElseThrow { NoSuchElementException("Measure not found") }!!

            // Get template if provided
            val template = measureRequest.templateId?.let {
                measureTemplateRepository.findById(it)
                    .orElseThrow { NoSuchElementException("Measure template not found") }!!
            }

            // Update measure
            val updatedMeasure = MeasureMapper.updateEntity(existingMeasure, measureRequest, template)
            val savedMeasure = measureRepository.save(updatedMeasure)

            MeasureMapper.toResponse(savedMeasure)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @Transactional
    override fun deleteMeasure(id: Long): Boolean {
        val measure = measureRepository.findById(id).orElseThrow {
            NoSuchElementException("Measure with ID $id not found")
        }!!

        measureRepository.delete(measure)

        return !measureRepository.existsById(id)
    }
}
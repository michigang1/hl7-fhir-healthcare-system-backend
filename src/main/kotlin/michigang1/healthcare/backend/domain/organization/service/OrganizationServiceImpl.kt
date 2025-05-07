package michigang1.healthcare.backend.domain.organization.service

import michigang1.healthcare.backend.domain.organization.Organization
import michigang1.healthcare.backend.domain.organization.payload.OrganizationResponse
import michigang1.healthcare.backend.domain.organization.repository.OrganizationRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux

@Service
class OrganizationServiceImpl(private val organizationRepository: OrganizationRepository): OrganizationService {
    override fun getAll(): Flux<List<OrganizationResponse>> = Mono.fromCallable {
        organizationRepository.findAll() }.subscribeOn(Schedulers.boundedElastic()).map { it.map { OrganizationResponse(it.id, it.name) } }.toFlux()

    override fun getById(id: Long): Mono<OrganizationResponse?> = Mono.fromCallable {
        organizationRepository.findById(id).orElseThrow() }.subscribeOn(Schedulers.boundedElastic()).map { OrganizationResponse(it.id, it.name) }



    override fun getByName(name: String) = Mono.fromCallable {
        organizationRepository.findByName(name) ?: throw NoSuchElementException("There are no organizations by this name") }
        .subscribeOn(Schedulers.boundedElastic()).map { OrganizationResponse(it.id, it.name) }


}
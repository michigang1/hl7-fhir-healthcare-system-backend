package michigang1.healthcare.backend.domain.organization.service

import michigang1.healthcare.backend.common.security.audit.AuditLogger
import michigang1.healthcare.backend.domain.organization.Organization
import michigang1.healthcare.backend.domain.organization.payload.OrganizationResponse
import michigang1.healthcare.backend.domain.organization.repository.OrganizationRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux

@Service
class OrganizationServiceImpl(
    private val organizationRepository: OrganizationRepository,
    private val auditLogger: AuditLogger
): OrganizationService {
    private val defaultUser = "system" // Default user for audit logging
    override fun getAll(): Flux<List<OrganizationResponse>> = Mono.fromCallable {
        organizationRepository.findAll() 
    }
    .subscribeOn(Schedulers.boundedElastic())
    .map { organizations ->
        val responses = organizations.map { OrganizationResponse(it.id, it.name) }
        // Log each organization retrieval
        responses.forEach { response ->
            response.id?.let { organizationId ->
                auditLogger.organizationRetrieved(organizationId, defaultUser)
            }
        }
        responses
    }
    .doOnError { auditLogger.organizationRetrievalFailed(defaultUser) }
    .toFlux()

    override fun getById(id: Long): Mono<OrganizationResponse?> = Mono.fromCallable {
        organizationRepository.findById(id).orElseThrow() 
    }
    .subscribeOn(Schedulers.boundedElastic())
    .map { OrganizationResponse(it.id, it.name) }
    .doOnSuccess { response -> 
        response?.id?.let { organizationId -> 
            auditLogger.organizationRetrieved(organizationId, defaultUser) 
        }
    }
    .doOnError { auditLogger.organizationRetrievalFailed(defaultUser) }



    override fun getByName(name: String) = Mono.fromCallable {
        organizationRepository.findByName(name) ?: throw NoSuchElementException("There are no organizations by this name") 
    }
    .subscribeOn(Schedulers.boundedElastic())
    .map { OrganizationResponse(it.id, it.name) }
    .doOnSuccess { response -> 
        response?.id?.let { organizationId -> 
            auditLogger.organizationRetrieved(organizationId, defaultUser) 
        }
    }
    .doOnError { auditLogger.organizationRetrievalFailed(defaultUser) }


}

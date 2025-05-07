package michigang1.healthcare.backend.domain.organization.service

import michigang1.healthcare.backend.domain.organization.Organization
import michigang1.healthcare.backend.domain.organization.payload.OrganizationResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrganizationService {
    fun getAll(): Flux<List<OrganizationResponse>>
    fun getById(id: Long): Mono<OrganizationResponse?>
    fun getByName(name: String): Mono<OrganizationResponse?>

}
package michigang1.healthcare.backend.adapter.fhir

import michigang1.healthcare.backend.domain.fhir.FhirService
import org.springframework.web.bind.annotation.RestController

@RestController
class FhirController(private val fhirService: FhirService) {
}
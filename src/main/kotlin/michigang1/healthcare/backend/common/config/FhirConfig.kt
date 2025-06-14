package michigang1.healthcare.backend.common.config

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.parser.IParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FhirConfig {

    @Bean
    fun fhirContext(): FhirContext {
        return FhirContext.forR4()
    }

    @Bean
    fun fhirJsonParser(fhirContext: FhirContext): IParser {
        return fhirContext.newJsonParser().setPrettyPrint(true)
    }
}

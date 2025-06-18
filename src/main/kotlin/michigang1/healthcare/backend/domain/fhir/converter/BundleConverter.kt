package michigang1.healthcare.backend.common.util.fhir

import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource

object BundleConverter {
    fun <T : Resource> createSearchSetBundle(resources: List<T>): Bundle {
        val bundle = Bundle()
        bundle.type = Bundle.BundleType.SEARCHSET
        
        resources.forEach { resource ->
            val entry = Bundle.BundleEntryComponent()
            entry.resource = resource
            bundle.addEntry(entry)
        }
        
        return bundle
    }
}
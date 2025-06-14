package michigang1.healthcare.backend.adapter.exception

import org.springframework.http.HttpStatus

class ResourceNotFoundException(message: String) : ApiException(
    httpStatus = HttpStatus.NOT_FOUND,
    apiError = ApiError(
        status = HttpStatus.NOT_FOUND.value(),
        error = "Resource not found",
        description = message
    )
)

class InvalidResourceException(message: String) : ApiException(
    httpStatus = HttpStatus.BAD_REQUEST,
    apiError = ApiError(
        status = HttpStatus.BAD_REQUEST.value(),
        error = "Invalid resource",
        description = message
    )
)

class FhirProcessingException(message: String) : ApiException(
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    apiError = ApiError(
        status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
        error = "FHIR processing error",
        description = message
    )
)
package michigang1.healthcare.backend.adapter.exception
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono
import java.util.NoSuchElementException
import java.util.concurrent.TimeoutException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsAuthException::class)
    fun handleUserAlreadyExistsException(exception: UsernameAlreadyExistsAuthException) = ResponseEntity(
        ApiError(
            status = exception.apiError.status,
            error = exception.apiError.error,
            description = exception.apiError.description
        ),
        exception.httpStatus
    )

    @ExceptionHandler(EmailAlreadyExistsAuthException::class)
    fun handleEmailAlreadyExistsException(exception: EmailAlreadyExistsAuthException) = ResponseEntity(
        ApiError(
            status = exception.apiError.status,
            error = exception.apiError.error,
            description = exception.apiError.description
        ),
        exception.httpStatus
    )

    @ExceptionHandler(BadCredentialsExceptionAuthException::class)
    fun handleBadCredentialsException(exception: BadCredentialsExceptionAuthException) = ResponseEntity(
        ApiError(
            status = exception.apiError.status,
            error = exception.apiError.error,
            description = exception.apiError.description
        ),
        exception.httpStatus
    )

    @ExceptionHandler(RoleNotFoundAuthException::class)
    fun handleRoleNotFoundException(exception: RoleNotFoundAuthException) = ResponseEntity(
        ApiError(
            status = exception.apiError.status,
            error = exception.apiError.error,
            description = exception.apiError.description
        ),
        exception.httpStatus
    )

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleMethodArgumentNotValidException(exception: WebExchangeBindException) = ResponseEntity(
        ApiError(
            status = exception.statusCode.value(),
            error = "Credentials are not valid",
            description = exception.fieldErrors.joinToString(". ") { "${it.defaultMessage}" }
        ),
        exception.statusCode
    )

    // FHIR-specific exception handlers
    @ExceptionHandler(ResourceNotFoundException::class, NoSuchElementException::class)
    fun handleResourceNotFoundException(exception: Exception) = ResponseEntity(
        ApiError(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Resource not found",
            description = exception.message ?: "Resource not found"
        ),
        HttpStatus.NOT_FOUND
    )

    @ExceptionHandler(InvalidResourceException::class, IllegalArgumentException::class)
    fun handleInvalidResourceException(exception: Exception) = ResponseEntity(
        ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Invalid resource",
            description = exception.message ?: "Invalid resource"
        ),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException) = ResponseEntity(
        ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Invalid request body",
            description = "Invalid request body: " + (exception.message ?: "Could not read HTTP message")
        ),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(exception: MethodArgumentTypeMismatchException) = ResponseEntity(
        ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Invalid argument type",
            description = "Invalid argument type: Parameter '${exception.name}' should be of type ${exception.requiredType?.simpleName}"
        ),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(FhirProcessingException::class)
    fun handleFhirProcessingException(exception: FhirProcessingException) = ResponseEntity(
        ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "FHIR processing error",
            description = exception.message ?: "FHIR processing error"
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
    )

    // Business logic exceptions
    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInputException(exception: ServerWebInputException) = ResponseEntity(
        ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Invalid input data",
            description = exception.message ?: "Invalid input data"
        ),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(exception: ResponseStatusException) = ResponseEntity(
        ApiError(
            status = exception.statusCode.value(),
            error = "Error with status code " + exception.statusCode,
            description = exception.reason ?: "Error with status code " + exception.statusCode
        ),
        exception.statusCode
    )

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(exception: AccessDeniedException) = ResponseEntity(
        ApiError(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Access denied",
            description = "Access denied: " + (exception.message ?: "Insufficient permissions")
        ),
        HttpStatus.FORBIDDEN
    )

    @ExceptionHandler(TimeoutException::class)
    fun handleTimeoutException(exception: TimeoutException) = ResponseEntity(
        ApiError(
            status = HttpStatus.GATEWAY_TIMEOUT.value(),
            error = "Operation timed out",
            description = exception.message ?: "Operation timed out"
        ),
        HttpStatus.GATEWAY_TIMEOUT
    )

    // Database-related exceptions
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(exception: org.springframework.dao.DataIntegrityViolationException) = ResponseEntity(
        ApiError(
            status = HttpStatus.CONFLICT.value(),
            error = "Data integrity violation",
            description = "Data integrity violation: possibly duplicate key or invalid data"
        ),
        HttpStatus.CONFLICT
    )

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultDataAccessException(exception: EmptyResultDataAccessException) = ResponseEntity(
        ApiError(
            status = HttpStatus.NOT_FOUND.value(),
            error = "No data found",
            description = "No data found: " + (exception.message ?: "Empty result")
        ),
        HttpStatus.NOT_FOUND
    )

    @ExceptionHandler(IncorrectResultSizeDataAccessException::class)
    fun handleIncorrectResultSizeDataAccessException(exception: IncorrectResultSizeDataAccessException) = ResponseEntity(
        ApiError(
            status = HttpStatus.CONFLICT.value(),
            error = "Incorrect result size",
            description = "Incorrect result size: " + (exception.message ?: "Multiple results found when expecting one")
        ),
        HttpStatus.CONFLICT
    )

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(exception: DataAccessException) = ResponseEntity(
        ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Database access error",
            description = "Database access error: " + (exception.message ?: "Unknown error")
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
    )

    // Reactive exceptions
    @ExceptionHandler(reactor.netty.channel.AbortedException::class)
    fun handleAbortedException(exception: reactor.netty.channel.AbortedException) = ResponseEntity(
        ApiError(
            status = HttpStatus.SERVICE_UNAVAILABLE.value(),
            error = "Operation aborted",
            description = "Operation aborted: " + (exception.message ?: "Unknown error")
        ),
        HttpStatus.SERVICE_UNAVAILABLE
    )

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(exception: IllegalStateException) = ResponseEntity(
        ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Illegal state",
            description = "Illegal state: " + (exception.message ?: "Unknown error")
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
    )

    @ExceptionHandler(java.util.concurrent.CancellationException::class)
    fun handleCancellationException(exception: java.util.concurrent.CancellationException) = ResponseEntity(
        ApiError(
            status = HttpStatus.SERVICE_UNAVAILABLE.value(),
            error = "Operation cancelled",
            description = "Operation cancelled: " + (exception.message ?: "Unknown error")
        ),
        HttpStatus.SERVICE_UNAVAILABLE
    )

    // Generic exception handler - should be the last one
    @ExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception) = ResponseEntity(
        ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Unexpected error",
            description = "Unexpected error: " + (exception.message ?: "Unknown error")
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
    )

}

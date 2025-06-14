package michigang1.healthcare.backend.adapter.exception

import ca.uhn.fhir.parser.IParser
import org.hl7.fhir.r4.model.OperationOutcome
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import java.util.NoSuchElementException

@RestControllerAdvice
class GlobalExceptionHandler(private val fhirJsonParser: IParser) {

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
    fun handleResourceNotFoundException(exception: Exception): ResponseEntity<String> {
        val outcome = createOperationOutcome(
            severity = OperationOutcome.IssueSeverity.ERROR,
            code = OperationOutcome.IssueType.NOTFOUND,
            diagnostics = exception.message ?: "Resource not found"
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(fhirJsonParser.encodeResourceToString(outcome))
    }

    @ExceptionHandler(InvalidResourceException::class, IllegalArgumentException::class)
    fun handleInvalidResourceException(exception: Exception): ResponseEntity<String> {
        val outcome = createOperationOutcome(
            severity = OperationOutcome.IssueSeverity.ERROR,
            code = OperationOutcome.IssueType.INVALID,
            diagnostics = exception.message ?: "Invalid resource"
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(fhirJsonParser.encodeResourceToString(outcome))
    }

    @ExceptionHandler(FhirProcessingException::class)
    fun handleFhirProcessingException(exception: FhirProcessingException): ResponseEntity<String> {
        val outcome = createOperationOutcome(
            severity = OperationOutcome.IssueSeverity.ERROR,
            code = OperationOutcome.IssueType.PROCESSING,
            diagnostics = exception.message ?: "FHIR processing error"
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(fhirJsonParser.encodeResourceToString(outcome))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception): ResponseEntity<String> {
        val outcome = createOperationOutcome(
            severity = OperationOutcome.IssueSeverity.ERROR,
            code = OperationOutcome.IssueType.EXCEPTION,
            diagnostics = exception.message ?: "An error occurred"
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(fhirJsonParser.encodeResourceToString(outcome))
    }

    private fun createOperationOutcome(
        severity: OperationOutcome.IssueSeverity,
        code: OperationOutcome.IssueType,
        diagnostics: String
    ): OperationOutcome {
        val outcome = OperationOutcome()
        val issue = OperationOutcome.OperationOutcomeIssueComponent()
        issue.severity = severity
        issue.code = code
        issue.diagnostics = diagnostics
        outcome.addIssue(issue)
        return outcome
    }
}

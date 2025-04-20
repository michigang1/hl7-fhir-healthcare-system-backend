package michigang1.healthcare.backend.adapter.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

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
}
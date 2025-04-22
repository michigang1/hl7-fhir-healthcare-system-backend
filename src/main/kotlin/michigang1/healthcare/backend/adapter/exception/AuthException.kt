package michigang1.healthcare.backend.adapter.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.support.WebExchangeBindException

class UsernameAlreadyExistsAuthException : ApiException(
    httpStatus = HttpStatus.BAD_REQUEST,
    apiError = ApiError(
        status = HttpStatus.BAD_REQUEST.value(),
        error = "Username already exists",
        description = "Username must be unique!"
    )
)
class EmailAlreadyExistsAuthException   : ApiException(
    httpStatus = HttpStatus.BAD_REQUEST,
    apiError = ApiError(
        status = HttpStatus.BAD_REQUEST.value(),
        error = "Email already exists",
        description = "Email must be unique!"
    )
)
class BadCredentialsExceptionAuthException : ApiException(
    httpStatus = HttpStatus.UNAUTHORIZED,
    apiError = ApiError(
        status = HttpStatus.UNAUTHORIZED.value(),
        error = "Bad credentials",
        description = "Invalid username or password!"
    )
)

class RoleNotFoundAuthException : ApiException(
    httpStatus = HttpStatus.NOT_FOUND,
    apiError = ApiError(
        status = HttpStatus.NOT_FOUND.value(),
        error = "Bad credentials",
        description = "Role must be one of the existing roles"
    )
)

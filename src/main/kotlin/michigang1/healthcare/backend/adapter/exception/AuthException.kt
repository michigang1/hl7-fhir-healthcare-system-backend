package michigang1.healthcare.backend.adapter.exception

import org.springframework.http.HttpStatus

class UsernameAlreadyExistsAuthException : ApiException(
    httpStatus = HttpStatus.BAD_REQUEST,
    apiError = ApiError(
        status = HttpStatus.BAD_REQUEST.value(),
        error = "Username already exists",
        description = "Error: Username is already taken!"
    )
)
class EmailAlreadyExistsAuthException   : ApiException(
    httpStatus = HttpStatus.BAD_REQUEST,
    apiError = ApiError(
        status = HttpStatus.BAD_REQUEST.value(),
        error = "Email already exists",
        description = "Error: Email is already in use!"
    )
)
class BadCredentialsExceptionAuthException : ApiException(
    httpStatus = HttpStatus.UNAUTHORIZED,
    apiError = ApiError(
        status = HttpStatus.UNAUTHORIZED.value(),
        error = "Bad credentials",
        description = "Error: Invalid username or password!"
    )
)

class RoleNotFoundAuthException : ApiException(
    httpStatus = HttpStatus.NOT_FOUND,
    apiError = ApiError(
        status = HttpStatus.NOT_FOUND.value(),
        error = "Role not found",
        description = "Error: Role not found!"
    )
)

package michigang1.healthcare.backend.adapter.exception

import org.springframework.http.HttpStatus

abstract class ApiException(
    val httpStatus: HttpStatus,
    val apiError: ApiError
) : RuntimeException(apiError.description)
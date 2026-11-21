package com.plexus.example.core.error.data.datasource.remote.model

import androidx.annotation.Keep

@Keep
open class ErrorResponseDTO(
    val response: String? = null,
    val error: ErrorDTO? = null,
    open val httpCode: Int
)

data class EmptyBodyErrorResponseDTO(
    override val httpCode: Int
) : ErrorResponseDTO(
    error = ErrorDTO.EmptyBodyErrorDTO,
    httpCode = httpCode
)

data class ExceptionErrorResponseDTO(
    override val httpCode: Int,
    val errorMessage: String
) : ErrorResponseDTO(
    error = ErrorDTO.ExceptionErrorDTO(message = errorMessage),
    httpCode = httpCode
)



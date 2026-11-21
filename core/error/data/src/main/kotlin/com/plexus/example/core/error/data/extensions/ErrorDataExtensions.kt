package com.plexus.example.core.error.data.extensions

import com.google.gson.Gson
import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.error.data.datasource.remote.model.ExceptionErrorResponseDTO
import com.plexus.example.core.error.domain.model.ErrorDomainModel.Companion.ERROR_UNKNOWN_MESSAGE
import retrofit2.Response

fun Throwable.toErrorResponseDTO(httpCode: Int): ExceptionErrorResponseDTO =
    ExceptionErrorResponseDTO(
        httpCode = httpCode,
        errorMessage = message ?: ERROR_UNKNOWN_MESSAGE
    )

fun Response<*>.toErrorResponse(): ErrorResponseDTO {
    val rawErrorBody = errorBody()?.string().orEmpty()
    val parsedResponse = try {
        Gson().fromJson(rawErrorBody, ErrorResponseDTO::class.java)
    } catch (e: Exception) {
        e.toErrorResponseDTO(code())
    }

    return parsedResponse
}
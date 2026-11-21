package com.plexus.example.core.error.domain.model

open class ErrorDomainModel(
    val code: Int,
    val message: String,
) {
    companion object {
        const val ERROR_UNKNOWN_CODE = -1
        const val ERROR_EXCEPTION_CODE = -2

        const val ERROR_UNKNOWN_MESSAGE = "Unknown error"
    }
}

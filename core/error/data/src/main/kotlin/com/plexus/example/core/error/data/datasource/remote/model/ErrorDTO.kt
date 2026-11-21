package com.plexus.example.core.error.data.datasource.remote.model

import androidx.annotation.Keep
import com.plexus.example.core.error.domain.model.ErrorDomainModel.Companion.ERROR_EXCEPTION_CODE

@Keep
open class ErrorDTO(
    val error: Int,
    open val message: String,
) {
    object EmptyBodyErrorDTO : ErrorDTO(
        error = -1,
        message = "Empty response body",
    )

    data class ExceptionErrorDTO(override val message: String) : ErrorDTO(
        code = ERROR_EXCEPTION_CODE,
        message = message,
    )
}
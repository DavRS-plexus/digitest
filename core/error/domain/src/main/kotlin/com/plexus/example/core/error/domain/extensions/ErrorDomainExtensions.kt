package com.plexus.example.core.error.domain.extensions

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.core.error.domain.model.ErrorDomainModel.Companion.ERROR_UNKNOWN_MESSAGE

fun Throwable.toErrorDomain(): ErrorDomainModel =
    ErrorDomainModel(
        code = ErrorDomainModel.ERROR_EXCEPTION_CODE,
        message = message ?: ERROR_UNKNOWN_MESSAGE
    )

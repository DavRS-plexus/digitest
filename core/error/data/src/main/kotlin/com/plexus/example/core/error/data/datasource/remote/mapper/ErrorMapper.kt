package com.plexus.example.core.error.data.datasource.remote.mapper

import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.error.domain.model.ErrorDomainModel

fun ErrorResponseDTO.toDomain(): ErrorDomainModel =
    ErrorDomainModel(
        code = error?.error ?: ErrorDomainModel.ERROR_UNKNOWN_CODE,
        message = error?.message ?: ErrorDomainModel.ERROR_UNKNOWN_MESSAGE
    )

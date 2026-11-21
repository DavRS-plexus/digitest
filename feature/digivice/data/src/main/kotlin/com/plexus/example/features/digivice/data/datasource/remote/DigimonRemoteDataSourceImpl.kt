package com.plexus.example.features.digivice.data.datasource.remote

import com.plexus.example.core.error.data.datasource.remote.model.EmptyBodyErrorResponseDTO
import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.error.data.extensions.toErrorResponse
import com.plexus.example.features.digivice.data.datasource.remote.api.DigimonApiService
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.libs.kotlinutils.Either
import com.plexus.example.libs.kotlinutils.Either.Companion.failure
import com.plexus.example.libs.kotlinutils.Either.Companion.success

class DigimonRemoteDataSourceImpl(
    private val apiService: DigimonApiService,
) : DigimonRemoteDataSource {
    override suspend fun getDigimonList(): Either<DigimonListDTO, String> {
        val response = apiService.getDigimons(
            pageSize = 20
        )

        val body = response.body()

        return when {
            !response.isSuccessful -> failure("KO")
            body == null -> failure("KO")
            else -> success(body)
        }
    }

    override suspend fun searchDigimon(name: String): Either<DigimonListDTO, String> {
        val response = apiService.searchDigimon(pageSize = 20, name = name)
        val body = response.body()

        return when {
            !response.isSuccessful -> failure("KO")
            body == null -> failure("Empty response body")
            else -> success(body)
        }
    }

    override suspend fun getDigimonDetail(id: String): Either<DigimonDetailDTO, ErrorResponseDTO> {
        val response = apiService.getDigimonDetail(id)
        val body = response.body()

        return when {
            !response.isSuccessful -> failure(response.toErrorResponse())
            body == null -> failure(EmptyBodyErrorResponseDTO(httpCode = response.code()))
            else -> success(body)
        }
    }
}

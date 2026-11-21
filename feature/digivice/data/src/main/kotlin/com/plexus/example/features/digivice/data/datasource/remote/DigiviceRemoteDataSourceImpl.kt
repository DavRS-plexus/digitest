package com.plexus.example.features.digivice.data.datasource.remote

import com.plexus.example.features.digivice.data.datasource.remote.api.DigimonApiService
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.libs.kotlinutils.Either
import com.plexus.example.libs.kotlinutils.Either.Companion.failure
import com.plexus.example.libs.kotlinutils.Either.Companion.success

class DigiviceRemoteDataSourceImpl(
    private val apiService: DigimonApiService,
) : DigiviceRemoteDataSource {
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
        val response = apiService.getDigimons(pageSize = 20, name = name)
        val body = response.body()

        return when {
            !response.isSuccessful -> failure("HTTP ${response.code()}")
            body == null -> failure("Empty response body")
            else -> success(body)
        }
    }
}

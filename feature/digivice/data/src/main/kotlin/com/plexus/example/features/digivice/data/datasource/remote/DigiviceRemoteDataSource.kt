package com.plexus.example.features.digivice.data.datasource.remote

import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.libs.kotlinutils.Either

interface DigiviceRemoteDataSource {
    suspend fun getDigimonList(): Either<DigimonListDTO, String>
    suspend fun searchDigimon(name: String): Either<DigimonListDTO, String>
    suspend fun getDigimonDetail(id: String): Either<DigimonDetailDTO, ErrorResponseDTO>
}

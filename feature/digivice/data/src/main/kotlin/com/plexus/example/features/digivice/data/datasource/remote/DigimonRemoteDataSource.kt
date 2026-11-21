package com.plexus.example.features.digivice.data.datasource.remote

import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.libs.kotlinutils.Either

interface DigimonRemoteDataSource {
    suspend fun getDigimonList(): Either<DigimonListDTO, ErrorResponseDTO>
    suspend fun searchDigimon(name: String): Either<DigimonListDTO, ErrorResponseDTO>
    suspend fun getDigimonDetail(id: String): Either<DigimonDetailDTO, ErrorResponseDTO>
}

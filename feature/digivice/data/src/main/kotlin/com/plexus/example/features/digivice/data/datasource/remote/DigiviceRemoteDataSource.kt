package com.plexus.example.features.digivice.data.datasource.remote

import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.libs.kotlinutils.Either

interface DigiviceRemoteDataSource {
    suspend fun getDigimonList(): Either<DigimonListDTO, String>
    suspend fun searchDigimon(name: String): Either<DigimonListDTO, String>
}


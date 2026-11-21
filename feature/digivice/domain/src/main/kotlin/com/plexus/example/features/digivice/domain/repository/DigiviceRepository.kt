package com.plexus.example.features.digivice.domain.repository

import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.Flow


interface DigiviceRepository {

    suspend fun getDigimonList(): Flow<Either<List<DigimonOverviewDomainModel>, String>>
    suspend fun searchDigimon(name: String): Flow<Either<DigimonListDomainModel, String>>
}

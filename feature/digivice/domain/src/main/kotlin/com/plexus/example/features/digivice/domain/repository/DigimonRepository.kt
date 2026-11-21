package com.plexus.example.features.digivice.domain.repository

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.Flow


interface DigimonRepository {

    suspend fun getDigimonList(): Flow<Either<List<DigimonOverviewDomainModel>, String>>
    suspend fun searchDigimon(name: String): Flow<Either<DigimonListDomainModel, String>>
    suspend fun getDigimonDetail(id: String): Flow<Either<DigimonDetailModel, ErrorDomainModel>>
}

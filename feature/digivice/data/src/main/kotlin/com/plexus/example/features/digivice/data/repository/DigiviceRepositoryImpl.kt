package com.plexus.example.features.digivice.data.repository

import com.plexus.example.core.error.data.datasource.remote.mapper.toDomain
import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.error.domain.extensions.toErrorDomain
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.data.datasource.remote.DigiviceRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.mapper.list.toDomain
import com.plexus.example.features.digivice.data.datasource.remote.mapper.detail.toDomain
import com.plexus.example.features.digivice.domain.repository.DigiviceRepository
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.libs.kotlinutils.Either
import com.plexus.example.libs.kotlinutils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DigiviceRepositoryImpl(
    private val remoteDataSource: DigiviceRemoteDataSource,
) : DigiviceRepository {
    override suspend fun getDigimonList() = flow {
        emit(
            remoteDataSource.getDigimonList().map(
                onSuccess = { it.toDomain().digimons },
                onFailure = { it }
            )
        )
    }.catch { throwable ->
        emit(Either.failure("KO"))
    }

    override suspend fun searchDigimon(name: String): Flow<Either<DigimonListDomainModel, String>> {
        return flow {
            emit(
                remoteDataSource.searchDigimon(name).map(
                    onSuccess = { it.toDomain() },
                    onFailure = { it },
                )
            )
        }.catch { throwable ->
            emit(Either.failure("KO"))
        }

    }

    override suspend fun getDigimonDetail(id: String): Flow<Either<DigimonDetailModel, ErrorDomainModel>> =
        flow {
            emit(remoteDataSource.getDigimonDetail(id).map(
                onSuccess = { it.toDomain() },
                onFailure = { it.toDomain() },
            ))
        }.catch { throwable ->
            emit(Either.failure(throwable.toErrorDomain()))
        }
}

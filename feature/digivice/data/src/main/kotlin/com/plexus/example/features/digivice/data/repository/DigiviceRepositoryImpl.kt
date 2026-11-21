package com.plexus.example.features.digivice.data.repository

import com.plexus.example.features.digivice.data.datasource.remote.DigiviceRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.mapper.list.toDomain
import com.plexus.example.features.digivice.domain.repository.DigiviceRepository
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
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
            emit(Either.failure(throwable.message ?: throwable.javaClass.simpleName))
        }
    }
}

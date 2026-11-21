package com.plexus.example.features.digivice.data.repository

import com.plexus.example.core.error.data.datasource.remote.mapper.toDomain
import com.plexus.example.core.error.domain.extensions.toErrorDomain
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.data.datasource.local.DigimonLocalDataSource
import com.plexus.example.features.digivice.data.datasource.local.mapper.toData
import com.plexus.example.features.digivice.data.datasource.local.mapper.toDomain
import com.plexus.example.features.digivice.data.datasource.remote.DigimonRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.mapper.detail.toDomain
import com.plexus.example.features.digivice.data.datasource.remote.mapper.list.toDomain
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import com.plexus.example.libs.kotlinutils.Either
import com.plexus.example.libs.kotlinutils.map
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class DigimonRepositoryImpl(
    private val remoteDataSource: DigimonRemoteDataSource,
    private val localDataSource: DigimonLocalDataSource,
) : DigimonRepository {

    override suspend fun getDigimonList(): Flow<Either<List<DigimonOverviewDomainModel>, ErrorDomainModel>> =
        flow {
            emit(
                remoteDataSource.getDigimonList().map(
                    onSuccess = { it.toDomain().digimons },
                    onFailure = { it.toDomain() }
                )
            )
        }.catch { throwable ->
            emit(Either.failure(throwable.toErrorDomain()))
        }

    override suspend fun searchDigimon(name: String): Flow<Either<DigimonListDomainModel, ErrorDomainModel>> {
        return flow {
            val localData = localDataSource.searchDigimon(name)

            if (localData != null) {
                emit(Either.success(localData.toDomain()))
            } else {
                emit(
                    remoteDataSource.searchDigimon(name).map(
                        onSuccess = {
                            it.toDomain().also {
                                localDataSource.saveDigimonList(name, it.toData())
                            }
                        },
                        onFailure = { it.toDomain() },
                    )
                )
            }
        }.catch { throwable ->
            emit(Either.failure(throwable.toErrorDomain()))
        }
    }

    override suspend fun getDigimonDetail(id: String): Flow<Either<DigimonDetailModel, ErrorDomainModel>> =
        flow {
            emit(
                remoteDataSource.getDigimonDetail(id).map(
                    onSuccess = { it.toDomain() },
                    onFailure = { it.toDomain() },
                )
            )
        }.catch { throwable ->
            emit(Either.failure(throwable.toErrorDomain()))
        }
}

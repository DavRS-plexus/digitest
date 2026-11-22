package com.plexus.example.features.digivice.data.di

import com.plexus.example.features.digivice.data.datasource.local.DigimonLocalDataSource
import com.plexus.example.features.digivice.data.datasource.remote.DigimonRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.DigimonRemoteDataSourceImpl
import com.plexus.example.features.digivice.data.datasource.remote.api.DigimonApiService
import com.plexus.example.features.digivice.data.repository.DigimonRepositoryImpl
import com.plexus.example.features.digivice.data.datasource.local.DigimonLocalDataSourceImpl
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import retrofit2.Retrofit

val digiviceDataModule = module {
    single<DigimonApiService> {
        get<Retrofit>().create(DigimonApiService::class.java)
    }
    single<DigimonRemoteDataSourceImpl>() bind DigimonRemoteDataSource::class
    single<DigimonRepositoryImpl>() bind DigimonRepository::class
    single<DigimonLocalDataSourceImpl>() bind DigimonLocalDataSource::class
}

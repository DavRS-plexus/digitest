package com.plexus.example.features.digivice.data.di

import com.plexus.example.features.digivice.data.datasource.remote.DigiviceRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.DigiviceRemoteDataSourceImpl
import com.plexus.example.features.digivice.data.datasource.remote.api.DigimonApiService
import com.plexus.example.features.digivice.data.repository.DigiviceRepositoryImpl
import com.plexus.example.features.digivice.domain.repository.DigiviceRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val digiviceDataModule = module {
    single<DigimonApiService> {
        get<Retrofit>().create(DigimonApiService::class.java)
    }
    single<DigiviceRemoteDataSource> {
        DigiviceRemoteDataSourceImpl(get())
    }
    single<DigiviceRepository> {
        DigiviceRepositoryImpl(get())
    }
}


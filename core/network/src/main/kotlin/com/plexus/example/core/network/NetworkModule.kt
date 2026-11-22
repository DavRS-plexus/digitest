package com.plexus.example.core.network

import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideHttpClient(): OkHttpClient {
    return OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}

fun provideConverterFactory(): GsonConverterFactory =
    GsonConverterFactory.create()

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    networkConfig: NetworkConfig,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(networkConfig.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}


val networkModule = module {
    single { create(::provideHttpClient) }
    single { create(::provideConverterFactory) }
    single { create(::provideRetrofit) }
}

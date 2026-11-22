package com.plexus.example.core.network

import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_READ_SECONDS = 60
private const val TIMEOUT_CONNECT_SECONDS = 60

fun provideHttpClient(): OkHttpClient {
    return OkHttpClient
        .Builder()
        .readTimeout(TIMEOUT_READ_SECONDS.toLong(), TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT_CONNECT_SECONDS.toLong(), TimeUnit.SECONDS)
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

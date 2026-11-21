package com.plexus.example.core.network

import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
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

// TODO: provide the url from build.gradle
fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://digi-api.com/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}


val networkModule = module {
    single { provideHttpClient() }
    single { provideConverterFactory() }
    single { provideRetrofit(get(),get()) }
}

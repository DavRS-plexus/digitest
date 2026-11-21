package com.plexus.example.features.digivice.data.datasource.remote.api

import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DigimonApiService {

    @GET("api/v1/digimon/")
    suspend fun getDigimons(
        @Query("pageSize") pageSize: Int? = null,
    ): Response<DigimonListDTO>

    @GET("api/v1/digimon/")
    suspend fun searchDigimon(
        @Query("pageSize") pageSize: Int? = null,
        @Query("name") name: String,
    ): Response<DigimonListDTO>

    @GET("api/v1/digimon/{id}")
    suspend fun getDigimonDetail(
        @Path("id") id: String
    ): Response<DigimonDetailDTO>
}

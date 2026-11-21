package com.plexus.example.features.digivice.data.datasource.local

import com.plexus.example.features.digivice.data.datasource.local.model.DigimonSearchDataModel

interface DigimonLocalDataSource {
    suspend fun searchDigimon(name: String): DigimonSearchDataModel?

    suspend fun saveDigimonList(name: String, data: DigimonSearchDataModel)
}

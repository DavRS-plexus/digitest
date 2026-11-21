package com.plexus.example.features.digivice.data.datasource.local.model

data class DigimonSearchDataModel(
    val digimons: List<DigimonOverviewDataModel>,
)

data class DigimonOverviewDataModel(
    val id: String,
    val name: String,
    val imageUrl: String,
)

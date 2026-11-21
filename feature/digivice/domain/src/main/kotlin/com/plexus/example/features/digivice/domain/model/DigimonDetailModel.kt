package com.plexus.example.features.digivice.domain.model

data class DigimonDetailModel(
    val name: String,
    val imageUrl: String?,
    val level: String,
    val types: List<String>,
    val attributes: List<String>,
    val description: String,
)
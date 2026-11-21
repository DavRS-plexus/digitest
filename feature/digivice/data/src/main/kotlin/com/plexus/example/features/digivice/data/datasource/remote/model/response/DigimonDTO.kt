package com.plexus.example.features.digivice.data.datasource.remote.model.response

import androidx.annotation.Keep

@Keep
data class DigimonDTO(
    val id: Int,
    val name: String,
    val href: String,
    val image: String,
)
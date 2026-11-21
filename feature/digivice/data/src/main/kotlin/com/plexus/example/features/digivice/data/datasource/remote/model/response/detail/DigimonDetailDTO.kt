package com.plexus.example.features.digivice.data.datasource.remote.model.response.detail

import androidx.annotation.Keep

@Keep
data class DigimonDetailDTO(
    val name: String,
    val images: List<DigimonImageDTO>,
    val levels: List<DigimonLevelDTO>,
    val types: List<DigimonTypeDTO>,
    val attributes: List<DigimonAttributeDTO>,
    val descriptions: List<DigimonDescriptionDTO>,
) {
    internal object DescriptionLanguage {
        const val ENGLISH = "en_us"
    }
}

@Keep
data class DigimonImageDTO(val href: String)

@Keep
data class DigimonLevelDTO(val level: String)

@Keep
data class DigimonTypeDTO(val type: String)

@Keep
data class DigimonAttributeDTO(val attribute: String)

@Keep
data class DigimonDescriptionDTO(val language: String, val description: String)

package com.plexus.example.features.digivice.data.datasource.remote.mapper.detail

import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel

internal fun DigimonDetailDTO.toDomain(): DigimonDetailModel =
    DigimonDetailModel(
        name = name,
        imageUrl = images.firstOrNull()?.href,
        level = levels.firstOrNull()?.level ?: "",
        types = types.map { it.type },
        attributes = attributes.map { it.attribute },
        description = descriptions.firstOrNull {
            it.language.equals(DigimonDetailDTO.DescriptionLanguage.ENGLISH, ignoreCase = true)
        }?.description ?: "",
    )

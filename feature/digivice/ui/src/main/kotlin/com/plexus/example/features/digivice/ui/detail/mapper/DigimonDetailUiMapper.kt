package com.plexus.example.features.digivice.ui.detail.mapper

import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel

internal fun DigimonDetailModel.toUi() = DigimonDetailUiModel(
    name = name,
    imageUrl = imageUrl,
    level = level,
    types = types,
    attributes = attributes,
    description = description,
)

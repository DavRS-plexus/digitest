package com.plexus.example.features.digivice.ui.search.mapper

import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel


internal fun DigimonOverviewDomainModel.toUi(): DigimonItemUiModel = DigimonItemUiModel(
    id = internalId,
    name = name,
    imageUrl = imageUrl
)
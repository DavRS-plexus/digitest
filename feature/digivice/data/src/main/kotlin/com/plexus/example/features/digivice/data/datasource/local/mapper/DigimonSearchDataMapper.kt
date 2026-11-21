package com.plexus.example.features.digivice.data.datasource.local.mapper

import com.plexus.example.features.digivice.data.datasource.local.model.DigimonOverviewDataModel
import com.plexus.example.features.digivice.data.datasource.local.model.DigimonSearchDataModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel

internal fun DigimonListDomainModel.toData() = DigimonSearchDataModel(
    digimons = digimons.map { DigimonOverviewDataModel(it.internalId, it.name, it.imageUrl) },
)

internal fun DigimonSearchDataModel.toDomain() = DigimonListDomainModel(
    digimons = digimons.map { DigimonOverviewDomainModel(it.id, it.name, it.imageUrl) },
)

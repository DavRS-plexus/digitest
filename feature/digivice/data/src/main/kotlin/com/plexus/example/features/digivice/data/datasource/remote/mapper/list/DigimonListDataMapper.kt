package com.plexus.example.features.digivice.data.datasource.remote.mapper.list

import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel

internal fun DigimonListDTO.toDomain(): DigimonListDomainModel =
    DigimonListDomainModel(
        digimons = content?.mapNotNull { runCatching { it.toDomain() }.getOrNull() } ?: emptyList(),
    )

private fun DigimonDTO.toDomain(): DigimonOverviewDomainModel =
    DigimonOverviewDomainModel(
        internalId = id.toString(),
        name = name,
        imageUrl = image,
    )
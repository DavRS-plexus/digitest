package com.plexus.example.features.digivice.data.datasource.remote.mapper.list

import com.plexus.example.features.digivice.data.datasource.remote.model.response.DigimonDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.PaginationDTO
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.model.PaginationDomainModel

internal fun DigimonListDTO.toDomain(): DigimonListDomainModel =
    DigimonListDomainModel(
        digimons = content.mapNotNull { runCatching { it.toDomain() }.getOrNull() },
        pagination = pageable.toDomain(),
    )

private fun DigimonDTO.toDomain(): DigimonOverviewDomainModel =
    DigimonOverviewDomainModel(
        internalId = id.toString(),
        name = name,
        imageUrl = image,
    )

private fun PaginationDTO.toDomain(): PaginationDomainModel =
    PaginationDomainModel(
        pageNumber = currentPage,
        pageSize = elementsOnPage,
        totalItems = totalElements,
        totalPages = totalPages,
    )
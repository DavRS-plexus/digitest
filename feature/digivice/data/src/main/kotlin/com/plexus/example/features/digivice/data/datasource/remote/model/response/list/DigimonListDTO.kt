package com.plexus.example.features.digivice.data.datasource.remote.model.response.list

import androidx.annotation.Keep

@Keep
data class DigimonListDTO(
    val content: List<DigimonDTO>,
    val pageable: PaginationDTO,
)

@Keep
data class DigimonDTO(
    val id: Int,
    val name: String,
    val href: String,
    val image: String,
)

@Keep
data class PaginationDTO(
    val currentPage: Int,
    val elementsOnPage: Int,
    val totalElements: Int,
    val totalPages: Int,
    val previousPage: String,
    val nextPage: String,
)

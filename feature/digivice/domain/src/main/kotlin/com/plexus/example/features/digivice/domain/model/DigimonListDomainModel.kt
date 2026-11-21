package com.plexus.example.features.digivice.domain.model

data class DigimonListDomainModel(
    val digimons: List<DigimonOverviewDomainModel>,
    val pagination: PaginationDomainModel,
)

data class PaginationDomainModel(
    val pageNumber: Int,
    val pageSize: Int,
    val totalItems: Int = 0,
    val totalPages: Int = 0
)

//data class PaginationDomainModel(
//    val pageNumber: Int,
//    val pageSize: Int,
//    val totalItems: Int = 0,
//)
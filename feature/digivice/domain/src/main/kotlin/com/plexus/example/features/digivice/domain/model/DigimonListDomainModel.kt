package com.plexus.example.features.digivice.domain.model

data class DigimonListDomainModel(
    val digimons: List<DigimonOverviewDomainModel>,
)

//data class PaginationDomainModel(
//    val pageNumber: Int,
//    val pageSize: Int,
//    val totalItems: Int = 0,
//)
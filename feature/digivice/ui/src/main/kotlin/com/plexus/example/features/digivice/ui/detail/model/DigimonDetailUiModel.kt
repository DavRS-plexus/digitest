package com.plexus.example.features.digivice.ui.detail.model

internal data class DigimonDetailUiModel(
    val name: String,
    val imageUrl: String?,
    val level: String,
    val types: List<String>,
    val attributes: List<String>,
    val description: String,
) {
    internal fun existImage() = imageUrl?.isNotBlank() == true
}

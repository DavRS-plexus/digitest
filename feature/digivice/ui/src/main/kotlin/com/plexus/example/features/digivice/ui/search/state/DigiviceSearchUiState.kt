package com.plexus.example.features.digivice.ui.search.state

import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel

internal data class DigiviceSearchUiState(
    val isLoading: Boolean = true,
    val items: List<DigimonItemUiModel> = emptyList(),
    val errorMessage: String? = null,
) {
    fun isErrorVisible() = errorMessage != null

    fun isEmptyList() = items.isEmpty() && !isLoading && errorMessage == null
}

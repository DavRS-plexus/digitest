package com.plexus.example.features.digivice.ui.detail.state

import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel

internal data class DigimonDetailUiState(
    val isLoading: Boolean = true,
    val detail: DigimonDetailUiModel? = null,
    val errorMessage: String? = null,
) {
    internal fun isErrorVisible() = errorMessage != null
}

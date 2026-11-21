package com.plexus.example.features.digivice.ui.search.state

internal sealed interface DigiviceSearchUiEvent {
    object NavigateToFilters : DigiviceSearchUiEvent
    object NavigateToSavings : DigiviceSearchUiEvent
    data class NavigateToDetail(val transactionId: String) : DigiviceSearchUiEvent
}
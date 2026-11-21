package com.plexus.example.features.digivice.ui.search.state

internal sealed interface DigiviceSearchUiEvent {
    data class NavigateToDetail(val digimonId: String) : DigiviceSearchUiEvent
}
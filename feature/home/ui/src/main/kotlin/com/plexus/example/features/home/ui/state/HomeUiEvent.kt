package com.plexus.example.features.home.ui.state

internal sealed interface HomeUiEvent {
    data object NavigateToDigivice : HomeUiEvent
}

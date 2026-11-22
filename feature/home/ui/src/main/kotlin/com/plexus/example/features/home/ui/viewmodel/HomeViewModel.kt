package com.plexus.example.features.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexus.example.features.home.ui.state.HomeUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class HomeViewModel : ViewModel() {
    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onAction(action: UserAction) {
        when (action) {
            UserAction.OnClickStart -> viewModelScope.launch {
                _uiEvent.emit(HomeUiEvent.NavigateToDigivice)
            }
        }
    }

    internal sealed interface UserAction {
        data object OnClickStart : UserAction
    }
}

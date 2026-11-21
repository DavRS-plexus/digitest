package com.plexus.example.features.digivice.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonListUseCase
import com.plexus.example.features.digivice.domain.usecase.SearchDigimonUseCase
import com.plexus.example.features.digivice.ui.search.mapper.toUi
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DigiviceSearchViewModel(
    private val getDigimonListUseCase: GetDigimonListUseCase,
    private val searchDigimonUseCase: SearchDigimonUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DigiviceSearchUiState(isLoading = true))
    val uiState: StateFlow<DigiviceSearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null


    init {
        println("DAVID = init DigiviceSearchViewModel")
        getDigimonList()
    }

    private fun getDigimonList() {
        searchJob?.cancel()
        _uiState.value = DigiviceSearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            getDigimonListUseCase().collect { result ->
                when (result) {
                    is Either.Success -> {
                        handleSuccessGetDigimonList(result.data)
                    }

                    is Either.Failure -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = "message"
                            )
                        }
                    }
                }
            }
        }
    }

    internal fun searchDigimon(name: String) {
        val query = name.trim()
        if (query.isEmpty()) {
            getDigimonList()
            return
        }

        searchJob?.cancel()
        _uiState.value = DigiviceSearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchDigimonUseCase(query).collect { result ->
                when (result) {
                    is Either.Success -> handleSuccessGetDigimonList(result.data.digimons)
                    is Either.Failure -> _uiState.update { currentState ->
                        currentState.copy(isLoading = false, errorMessage = result.fail)
                    }
                }
            }
        }
    }

    private fun handleSuccessGetDigimonList(digimons: List<DigimonOverviewDomainModel>) {
        println("DAVID = Success: $digimons")
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                items = digimons.map { it.toUi() }
            )
        }
    }

//    // region User Actions
//
//    internal fun onAction(action: UserAction) {
//        when (action) {
//            is UserAction.OnClick -> {}
//        }
//    }

//
//    // endregion
//
//    internal sealed class UserAction {
//        object OnClick : UserAction()
//    }
//
//    internal companion object {
//        private const val DEFAULT_PAGE_SIZE = 20
//    }
}
package com.plexus.example.features.digivice.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonListUseCase
import com.plexus.example.features.digivice.domain.usecase.SearchDigimonUseCase
import com.plexus.example.features.digivice.ui.search.mapper.toUi
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiEvent
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiEvent.NavigateToDetail
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class DigiviceSearchViewModel(
    private val getDigimonListUseCase: GetDigimonListUseCase,
    private val searchDigimonUseCase: SearchDigimonUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DigiviceSearchUiState(isLoading = true))
    val uiState: StateFlow<DigiviceSearchUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DigiviceSearchUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var searchJob: Job? = null
    private var oldQuery: String = ""


    init {
        getDigimonList()
    }

    private fun getDigimonList() {
        _uiState.value = DigiviceSearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(dispatcher) {
            getDigimonListUseCase().collect { result ->
                when (result) {
                    is Either.Success -> handleSuccessGetDigimonList(result.data)
                    is Either.Failure -> handleFailureGetDigimonList(result.fail)
                }
            }
        }
    }

    private fun handleSuccessGetDigimonList(digimons: List<DigimonOverviewDomainModel>) {
        _uiState.value =
            DigiviceSearchUiState(isLoading = false, items = digimons.map { it.toUi() })
    }

    private fun handleFailureGetDigimonList(errorModel: ErrorDomainModel) {
        _uiState.value = DigiviceSearchUiState(isLoading = false, errorMessage = errorModel.message)
    }

    internal fun checkQuery(name: String) {
        val query = name.trim()

        when {
            query == oldQuery -> return
            query.isBlank() -> {
                updateQuery("")
                getDigimonList()
                return
            }

            else -> {
                updateQuery(query)
                searchDigimon(query)
            }
        }
    }

    private fun updateQuery(newQuery: String) {
        searchJob?.cancel()
        oldQuery = newQuery
    }

    private fun searchDigimon(name: String) {
        _uiState.value = DigiviceSearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(dispatcher) {
            searchDigimonUseCase(name).collect { result ->
                when (result) {
                    is Either.Success -> handleSuccessGetDigimonList(result.data.digimons)
                    is Either.Failure -> handleFailureGetDigimonList(result.fail)
                }
            }
        }
    }

    // region User Actions

    internal fun onAction(action: UserAction) {
        when (action) {
            is UserAction.OnClickSearch -> checkQuery(action.name)
            is UserAction.OnClickItem -> {
                viewModelScope.launch(dispatcher) {
                    _uiEvent.emit(NavigateToDetail(action.id))
                }
            }
        }
    }

    internal sealed class UserAction {
        data class OnClickSearch(val name: String) : UserAction()
        data class OnClickItem(val id: String) : UserAction()
    }

    // endregion
}
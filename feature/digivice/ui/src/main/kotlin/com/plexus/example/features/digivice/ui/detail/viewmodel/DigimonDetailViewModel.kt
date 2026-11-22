package com.plexus.example.features.digivice.ui.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonDetailUseCase
import com.plexus.example.features.digivice.ui.detail.mapper.toUi
import com.plexus.example.features.digivice.ui.detail.state.DigimonDetailUiState
import com.plexus.example.features.digivice.ui.navigation.DigiviceInternalRoute.DigiviceDetailRoute
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class DigimonDetailViewModel(
    private val getDigimonDetailUseCase: GetDigimonDetailUseCase,
    savedStateHandle: SavedStateHandle,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DigimonDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()


    init {
        val digimonId = savedStateHandle.toRoute<DigiviceDetailRoute>().digimonId
        getDigimonDetail(digimonId)
    }

    internal fun getDigimonDetail(id: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = DigimonDetailUiState(isLoading = true)

            getDigimonDetailUseCase(id).collect { result ->
                when (result) {
                    is Either.Success -> handleSuccessGetDigimonDetail(result.data)
                    is Either.Failure -> handleFailureGetDigimonDetail(result.fail)
                }
            }
        }
    }

    private fun handleSuccessGetDigimonDetail(detail: DigimonDetailModel) {
        _uiState.value = DigimonDetailUiState(isLoading = false, detail = detail.toUi())
    }

    private fun handleFailureGetDigimonDetail(errorModel: ErrorDomainModel) {
        _uiState.value = DigimonDetailUiState(isLoading = false, errorMessage = errorModel.message)
    }
}

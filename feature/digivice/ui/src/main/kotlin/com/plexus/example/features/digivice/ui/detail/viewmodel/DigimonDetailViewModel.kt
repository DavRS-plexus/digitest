package com.plexus.example.features.digivice.ui.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonDetailUseCase
import com.plexus.example.features.digivice.ui.detail.mapper.toUi
import com.plexus.example.features.digivice.ui.detail.state.DigimonDetailUiState
import com.plexus.example.features.digivice.ui.navigation.DigiviceInternalRoute
import com.plexus.example.libs.androidutils.extensions.launchIO
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class DigimonDetailViewModel(
    private val getDigimonDetailUseCase: GetDigimonDetailUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DigimonDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()


    init {
        val digimonId =
            savedStateHandle.toRoute<DigiviceInternalRoute.DigiviceDetailRoute>().digimonId
        getDigimonDetail(digimonId)
    }

    internal fun getDigimonDetail(id: String) {
        launchIO {
            _uiState.value = DigimonDetailUiState(isLoading = true)

            getDigimonDetailUseCase(id).collect { result ->
                when (result) {
                    is Either.Success -> handleSuccessGetDigimonDetail(result.data)
                    is Either.Failure -> handleFailureGetDigimonList(result.fail)
                }
            }
        }
    }

    private fun handleSuccessGetDigimonDetail(detail: DigimonDetailModel) {
        _uiState.value = DigimonDetailUiState(isLoading = false, detail = detail.toUi())
    }

    private fun handleFailureGetDigimonList(errorModel: ErrorDomainModel) {
        _uiState.value = DigimonDetailUiState(isLoading = false, errorMessage = errorModel.message)
    }
}

package com.plexus.example.features.digivice.ui.detail.viewmodel

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonDetailUseCase
import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel
import com.plexus.example.features.digivice.ui.detail.state.DigimonDetailUiState
import com.plexus.example.libs.kotlinutils.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DigimonDetailViewModelTest {
    private val getDigimonDetailUseCase = mockk<GetDigimonDetailUseCase>()
    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DigimonDetailViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        // This is needed because the viewModel load a param of the savedStateHandle in the init
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putCharSequence(any(), any()) } answers {
            val key = firstArg<String>()
            val value = secondArg<CharSequence?>()
            every { anyConstructed<Bundle>().containsKey(key) } returns true
            every { anyConstructed<Bundle>().get(key) } returns value
            every { anyConstructed<Bundle>().getString(key) } returns value?.toString()
        }
    }

    @AfterEach
    fun tearDown() {
        if (::viewModel.isInitialized) viewModel.viewModelScope.cancel()
        unmockkConstructor(Bundle::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN a pending request, WHEN navigates to the detail screen, THEN shows loading`() =
        runTest(UnconfinedTestDispatcher()) {
            // GIVEN
            coEvery { getDigimonDetailUseCase("1") } returns flow {
                awaitCancellation()
            }

            // WHEN
            viewModel = createViewModel("1")
            advanceUntilIdle()

            // THEN
            assertEquals(DigimonDetailUiState(isLoading = true), viewModel.uiState.value)
            coVerify(exactly = 1) { getDigimonDetailUseCase("1") }
        }

    @Test
    fun `GIVEN a successful result, WHEN get the detailed digimon, THEN displays the mapped detail`() =
        runTest {
            // GIVEN
            val detail = DigimonDetailModel(
                "Agumon", "image", "Rookie", listOf("Reptile"), listOf("Vaccine"), "Description",
            )
            val expected = DigimonDetailUiState(
                isLoading = false,
                detail = DigimonDetailUiModel(
                    "Agumon",
                    "image",
                    "Rookie",
                    listOf("Reptile"),
                    listOf("Vaccine"),
                    "Description",
                ),
            )
            coEvery { getDigimonDetailUseCase("1") } returns flowOf(Either.Success(detail))

            // WHEN
            viewModel = createViewModel("1")
            advanceUntilIdle()

            // THEN
            assertEquals(expected, viewModel.uiState.value)
            coVerify(exactly = 1) { getDigimonDetailUseCase("1") }
        }

    @Test
    fun `GIVEN a failed result, WHEN get the detailed digimon, THEN displays the error message`() =
        runTest {
            // GIVEN
            val error = ErrorDomainModel(404, "Not found")
            val expected = DigimonDetailUiState(isLoading = false, errorMessage = "Not found")
            coEvery { getDigimonDetailUseCase("2") } returns flowOf(Either.Failure(error))

            // WHEN
            viewModel = createViewModel("2")
            advanceUntilIdle()

            // THEN
            assertEquals(expected, viewModel.uiState.value)
            coVerify(exactly = 1) { getDigimonDetailUseCase("2") }
        }

    private fun createViewModel(id: String) =
        DigimonDetailViewModel(getDigimonDetailUseCase, SavedStateHandle(mapOf("digimonId" to id)), dispatcher)
}

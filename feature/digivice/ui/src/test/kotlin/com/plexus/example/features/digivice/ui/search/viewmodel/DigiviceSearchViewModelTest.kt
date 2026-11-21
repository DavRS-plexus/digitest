package com.plexus.example.features.digivice.ui.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonListUseCase
import com.plexus.example.features.digivice.domain.usecase.SearchDigimonUseCase
import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiEvent
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState
import com.plexus.example.libs.kotlinutils.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DigiviceSearchViewModelTest {
    private val getDigimonListUseCase = mockk<GetDigimonListUseCase>()
    private val searchDigimonUseCase = mockk<SearchDigimonUseCase>()
    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DigiviceSearchViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        if (::viewModel.isInitialized) viewModel.viewModelScope.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN a pending list request, WHEN navigates to the search screen, THEN shows loading`() = runTest {
        // GIVEN
        coEvery { getDigimonListUseCase() } returns flow { awaitCancellation() }

        // WHEN
        viewModel = createViewModel()
        advanceUntilIdle()

        // THEN
        assertEquals(DigiviceSearchUiState(isLoading = true), viewModel.uiState.value)
        coVerify(exactly = 1) { getDigimonListUseCase() }
    }

    @Test
    fun `GIVEN a successful list request, WHEN get the digimon list, THEN displays mapped items`() = runTest {
        // GIVEN
        val digimons = listOf(DigimonOverviewDomainModel("1", "Agumon", "image"))
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(digimons))

        // WHEN
        viewModel = createViewModel()
        advanceUntilIdle()

        // THEN
        assertEquals(
            DigiviceSearchUiState(isLoading = false, items = listOf(DigimonItemUiModel("1", "Agumon", "image"))),
            viewModel.uiState.value,
        )
        coVerify(exactly = 1) { getDigimonListUseCase() }
    }

    @Test
    fun `GIVEN a failed list request, WHEN get the digimon list, THEN displays the error`() = runTest {
        // GIVEN
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Failure(ErrorDomainModel(500, "List failed")))

        // WHEN
        viewModel = createViewModel()
        advanceUntilIdle()

        // THEN
        assertEquals(
            DigiviceSearchUiState(isLoading = false, errorMessage = "List failed"),
            viewModel.uiState.value
        )
        coVerify(exactly = 1) { getDigimonListUseCase() }
    }

    @Test
    fun `GIVEN a success search request, WHEN searching with surrounding whitespace, THEN searches the trimmed name and displays items`() = runTest {
        // GIVEN
        val digimons = DigimonListDomainModel(listOf(DigimonOverviewDomainModel("1", "Agumon", "image")))
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(emptyList()))
        coEvery { searchDigimonUseCase("Agumon") } returns flowOf(Either.Success(digimons))
        viewModel = createViewModel()
        advanceUntilIdle()

        // WHEN
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("  Agumon  "))
        val loading = viewModel.uiState.value
        advanceUntilIdle()
        val success = viewModel.uiState.value

        // THEN
        assertEquals(DigiviceSearchUiState(isLoading = true), loading)
        assertEquals(
            DigiviceSearchUiState(isLoading = false, items = listOf(DigimonItemUiModel("1", "Agumon", "image"))),
            success
        )
        coVerify(exactly = 1) { searchDigimonUseCase("Agumon") }
    }

    @Test
    fun `GIVEN a failed search request, WHEN searching by name, THEN displays the error`() = runTest {
        // GIVEN
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(emptyList()))
        coEvery { searchDigimonUseCase("Agumon") } returns flowOf(Either.Failure(ErrorDomainModel(500, "Search failed")))
        viewModel = createViewModel()
        advanceUntilIdle()

        // WHEN
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("Agumon"))
        advanceUntilIdle()

        // THEN
        assertEquals(DigiviceSearchUiState(isLoading = false, errorMessage = "Search failed"), viewModel.uiState.value)
        coVerify(exactly = 1) { searchDigimonUseCase("Agumon") }
    }

    @Test
    fun `GIVEN an active search, WHEN searching the same trimmed name, THEN keeps the request running`() = runTest {
        // GIVEN
        val response = CompletableDeferred<DigimonListDomainModel>()
        val digimons = DigimonListDomainModel(listOf(DigimonOverviewDomainModel("1", "Agumon", "image")))
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(emptyList()))
        coEvery { searchDigimonUseCase("Agumon") } returns flow { emit(Either.Success(response.await())) }
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("Agumon"))
        advanceUntilIdle()

        // WHEN
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch(" Agumon "))
        response.complete(digimons)
        advanceUntilIdle()

        // THEN
        assertEquals(
            DigiviceSearchUiState(isLoading = false, items = listOf(DigimonItemUiModel("1", "Agumon", "image"))),
            viewModel.uiState.value,
        )
        coVerify(exactly = 1) { searchDigimonUseCase("Agumon") }
    }

    @Test
    fun `GIVEN a previous search, WHEN submitting a blank name, THEN reloads the full list`() = runTest {
        // GIVEN
        val list = listOf(DigimonOverviewDomainModel("2", "Gabumon", "image"))
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(list))
        coEvery { searchDigimonUseCase("Agumon") } returns flowOf(
            Either.Success(DigimonListDomainModel(emptyList()))
        )
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("Agumon"))
        advanceUntilIdle()

        // WHEN
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("  "))
        advanceUntilIdle()

        // THEN
        assertEquals(
            DigiviceSearchUiState(isLoading = false, items = listOf(DigimonItemUiModel("2", "Gabumon", "image"))),
            viewModel.uiState.value,
        )
        coVerify(exactly = 2) { getDigimonListUseCase() }
    }

    @Test
    fun `GIVEN an active search, WHEN searching a different name, THEN cancels the previous search`() = runTest {
        // GIVEN
        val cancelled = CompletableDeferred<Unit>()
        val digimons = DigimonListDomainModel(listOf(DigimonOverviewDomainModel("2", "Gabumon", "image")))
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(emptyList()))
        coEvery { searchDigimonUseCase("Agumon") } returns flow {
            try {
                awaitCancellation()
            } finally {
                cancelled.complete(Unit)
            }
        }
        coEvery { searchDigimonUseCase("Gabumon") } returns flowOf(Either.Success(digimons))
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("Agumon"))
        advanceUntilIdle()

        // WHEN
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickSearch("Gabumon"))
        advanceUntilIdle()

        // THEN
        assertTrue(cancelled.isCompleted)
        assertEquals(
            DigiviceSearchUiState(
                isLoading = false,
                items = listOf(DigimonItemUiModel("2", "Gabumon", "image"))
            ),
            viewModel.uiState.value,
        )
        coVerify(exactly = 1) { searchDigimonUseCase("Agumon") }
        coVerify(exactly = 1) { searchDigimonUseCase("Gabumon") }
    }

    @Test
    fun `GIVEN an event collector, WHEN an item is clicked, THEN emits navigation with its id`() = runTest {
        // GIVEN
        coEvery { getDigimonListUseCase() } returns flowOf(Either.Success(emptyList()))
        viewModel = createViewModel()
        val event = CompletableDeferred<DigiviceSearchUiEvent>()
        val collector = backgroundScope.launch { event.complete(viewModel.uiEvent.first()) }
        runCurrent()

        // WHEN
        viewModel.onAction(DigiviceSearchViewModel.UserAction.OnClickItem("123"))
        runCurrent()

        // THEN
        assertTrue(event.isCompleted)
        assertEquals(DigiviceSearchUiEvent.NavigateToDetail("123"), event.await())
        collector.cancel()
    }

    private fun createViewModel() =
        DigiviceSearchViewModel(getDigimonListUseCase, searchDigimonUseCase, dispatcher)
}

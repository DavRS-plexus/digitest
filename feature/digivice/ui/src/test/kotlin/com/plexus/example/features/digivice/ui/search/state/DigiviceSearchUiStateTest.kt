package com.plexus.example.features.digivice.ui.search.state

import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DigiviceSearchUiStateTest {
    @Test
    fun `GIVEN the default state, WHEN checking visibility, THEN neither an error nor an empty list is shown`() {
        // GIVEN
        val state = DigiviceSearchUiState()

        // WHEN
        val errorVisible = state.isErrorVisible()
        val emptyList = state.isEmptyList()

        // THEN
        assertTrue(state.isLoading)
        assertFalse(errorVisible)
        assertFalse(emptyList)
    }

    @Test
    fun `GIVEN a finished search without items or an error, WHEN checking for an empty list, THEN it is shown`() {
        // GIVEN
        val state = DigiviceSearchUiState(isLoading = false)

        // WHEN
        val result = state.isEmptyList()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a finished search with items, WHEN checking for an empty list, THEN it is not shown`() {
        // GIVEN
        val state = DigiviceSearchUiState(
            isLoading = false,
            items = listOf(DigimonItemUiModel("1", "Agumon", "image")),
        )

        // WHEN
        val result = state.isEmptyList()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN an error message and no items, WHEN checking visibility, THEN the error is shown instead of an empty list`() {
        // GIVEN
        val state = DigiviceSearchUiState(isLoading = false, errorMessage = "Request failed")

        // WHEN
        val errorVisible = state.isErrorVisible()
        val emptyList = state.isEmptyList()

        // THEN
        assertTrue(errorVisible)
        assertFalse(emptyList)
    }

    @Test
    fun `GIVEN a blank error message, WHEN checking visibility, THEN the error is shown instead of an empty list`() {
        // GIVEN
        val state = DigiviceSearchUiState(isLoading = false, errorMessage = "")

        // WHEN
        val errorVisible = state.isErrorVisible()
        val emptyList = state.isEmptyList()

        // THEN
        assertTrue(errorVisible)
        assertFalse(emptyList)
    }

    @Test
    fun `GIVEN items without an error, WHEN checking for an error, THEN it is not shown`() {
        // GIVEN
        val state = DigiviceSearchUiState(
            isLoading = false,
            items = listOf(DigimonItemUiModel("1", "Agumon", "image")),
        )

        // WHEN
        val result = state.isErrorVisible()

        // THEN
        assertFalse(result)
    }
}

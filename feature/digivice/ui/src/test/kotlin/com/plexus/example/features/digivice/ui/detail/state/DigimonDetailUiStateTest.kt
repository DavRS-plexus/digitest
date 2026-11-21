package com.plexus.example.features.digivice.ui.detail.state

import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DigimonDetailUiStateTest {
    @Test
    fun `GIVEN the default state, WHEN checking for an error, THEN it is not shown`() {
        // GIVEN
        val state = DigimonDetailUiState()

        // WHEN
        val result = state.isErrorVisible()

        // THEN
        assertTrue(state.isLoading)
        assertNull(state.detail)
        assertFalse(result)
    }

    @Test
    fun `GIVEN a detail without an error, WHEN checking for an error, THEN it is not shown`() {
        // GIVEN
        val detail = DigimonDetailUiModel("Agumon", "image", "Rookie", emptyList(), emptyList(), "")
        val state = DigimonDetailUiState(isLoading = false, detail = detail)

        // WHEN
        val result = state.isErrorVisible()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN an error message, WHEN checking for an error, THEN it is shown`() {
        // GIVEN
        val state = DigimonDetailUiState(isLoading = false, errorMessage = "Request failed")

        // WHEN
        val result = state.isErrorVisible()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a blank error message, WHEN checking for an error, THEN it is shown`() {
        // GIVEN
        val state = DigimonDetailUiState(isLoading = false, errorMessage = "")

        // WHEN
        val result = state.isErrorVisible()

        // THEN
        assertTrue(result)
    }
}

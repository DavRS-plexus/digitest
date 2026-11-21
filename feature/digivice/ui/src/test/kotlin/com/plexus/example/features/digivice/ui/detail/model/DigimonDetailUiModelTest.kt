package com.plexus.example.features.digivice.ui.detail.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DigimonDetailUiModelTest {
    @Test
    fun `GIVEN an image URL, WHEN checking if an image exists, THEN returns true`() {
        // GIVEN
        val model = detailWithImage("https://example.com/agumon.png")

        // WHEN
        val result = model.existImage()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a null image URL, WHEN checking if an image exists, THEN returns false`() {
        // GIVEN
        val model = detailWithImage(null)

        // WHEN
        val result = model.existImage()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN an empty image URL, WHEN checking if an image exists, THEN returns false`() {
        // GIVEN
        val model = detailWithImage("")

        // WHEN
        val result = model.existImage()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN a whitespace image URL, WHEN checking if an image exists, THEN returns false`() {
        // GIVEN
        val model = detailWithImage(" \t\n")

        // WHEN
        val result = model.existImage()

        // THEN
        assertFalse(result)
    }

    private fun detailWithImage(imageUrl: String?) =
        DigimonDetailUiModel("Agumon", imageUrl, "Rookie", emptyList(), emptyList(), "")
}

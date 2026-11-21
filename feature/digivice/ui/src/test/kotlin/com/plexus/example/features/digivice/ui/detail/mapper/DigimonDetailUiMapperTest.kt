package com.plexus.example.features.digivice.ui.detail.mapper

import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DigimonDetailUiMapperTest {
    @Test
    fun `GIVEN a detailed digimon, WHEN toUi is called, THEN maps all fields`() {
        // GIVEN
        val domain = DigimonDetailModel(
            "Agumon", "agumon.png", "Rookie", listOf("Reptile", "Dragon"),
            listOf("Vaccine", "Data"), "A small dinosaur Digimon",
        )

        // WHEN
        val result = domain.toUi()

        // THEN
        assertEquals(
            DigimonDetailUiModel(
                "Agumon", "agumon.png", "Rookie", listOf("Reptile", "Dragon"),
                listOf("Vaccine", "Data"), "A small dinosaur Digimon",
            ),
            result,
        )
    }

    @Test
    fun `GIVEN blank details and no image, WHEN toUi is called, THEN preserves empty values`() {
        // GIVEN
        val domain = DigimonDetailModel("", null, "", emptyList(), emptyList(), "")

        // WHEN
        val result = domain.toUi()

        // THEN
        assertEquals(DigimonDetailUiModel("", null, "", emptyList(), emptyList(), ""), result)
    }
}

package com.plexus.example.features.digivice.ui.search.mapper

import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DigimonItemUiMapperTest {
    @Test
    fun `GIVEN a digimon overview, WHEN toUi is called, THEN maps all fields`() {
        // GIVEN
        val domain = DigimonOverviewDomainModel("1", "Agumon", "agumon.png")

        // WHEN
        val result = domain.toUi()

        // THEN
        assertEquals(DigimonItemUiModel("1", "Agumon", "agumon.png"), result)
    }

    @Test
    fun `GIVEN blank overview fields, WHEN toUi is called, THEN preserves blank values`() {
        // GIVEN
        val domain = DigimonOverviewDomainModel("", "", "")

        // WHEN
        val result = domain.toUi()

        // THEN
        assertEquals(DigimonItemUiModel("", "", ""), result)
    }
}

package com.plexus.example.features.digivice.data.datasource.local.mapper

import com.plexus.example.features.digivice.data.datasource.local.model.DigimonOverviewDataModel
import com.plexus.example.features.digivice.data.datasource.local.model.DigimonSearchDataModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DigimonSearchDataMapperTest {
    @Test
    fun `GIVEN domain digimons, WHEN toData is called, THEN maps all fields properly`() {
        // GIVEN
        val domain = DigimonListDomainModel(
            listOf(
                DigimonOverviewDomainModel("1", "Agumon", "agumon.png"),
                DigimonOverviewDomainModel("2", "Gabumon", "gabumon.png"),
            ),
        )

        // WHEN
        val result = domain.toData()

        // THEN
        assertEquals(
            DigimonSearchDataModel(
                listOf(
                    DigimonOverviewDataModel("1", "Agumon", "agumon.png"),
                    DigimonOverviewDataModel("2", "Gabumon", "gabumon.png"),
                ),
            ),
            result,
        )
    }

    @Test
    fun `GIVEN stored digimons, WHEN toDomain is called, THEN maps all fields properly`() {
        // GIVEN
        val data = DigimonSearchDataModel(
            listOf(
                DigimonOverviewDataModel("1", "Agumon", "agumon.png"),
                DigimonOverviewDataModel("2", "Gabumon", "gabumon.png"),
            ),
        )

        // WHEN
        val result = data.toDomain()

        // THEN
        assertEquals(
            DigimonListDomainModel(
                listOf(
                    DigimonOverviewDomainModel("1", "Agumon", "agumon.png"),
                    DigimonOverviewDomainModel("2", "Gabumon", "gabumon.png"),
                ),
            ),
            result,
        )
    }

    @Test
    fun `GIVEN an empty domain list, WHEN toData is called, THEN returns an empty data list`() {
        // GIVEN
        val domain = DigimonListDomainModel(emptyList())

        // WHEN
        val result = domain.toData()

        // THEN
        assertEquals(DigimonSearchDataModel(emptyList()), result)
    }

    @Test
    fun `GIVEN an empty data list, WHEN toDomain is called, THEN returns an empty domain list`() {
        // GIVEN
        val data = DigimonSearchDataModel(emptyList())

        // WHEN
        val result = data.toDomain()

        // THEN
        assertEquals(DigimonListDomainModel(emptyList()), result)
    }
}

package com.plexus.example.features.digivice.data.datasource.remote.mapper.detail

import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonAttributeDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDescriptionDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonImageDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonLevelDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonTypeDTO
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DigimonDetailDataMapperTest {
    @Test
    fun `GIVEN a detailed digimon, WHEN toDomain is called, THEN maps fields and selects the first English description`() {
        // GIVEN
        val response = DigimonDetailDTO(
            name = "Agumon",
            images = listOf(DigimonImageDTO("first.png"), DigimonImageDTO("second.png")),
            levels = listOf(DigimonLevelDTO("Rookie"), DigimonLevelDTO("Champion")),
            types = listOf(DigimonTypeDTO("Reptile"), DigimonTypeDTO("Dragon")),
            attributes = listOf(DigimonAttributeDTO("Vaccine"), DigimonAttributeDTO("Data")),
            descriptions = listOf(
                DigimonDescriptionDTO("ja_jp", "Japanese description"),
                DigimonDescriptionDTO("EN_US", "First English description"),
                DigimonDescriptionDTO("en_us", "Second English description"),
            ),
        )

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals(
            DigimonDetailModel(
                name = "Agumon",
                imageUrl = "first.png",
                level = "Rookie",
                types = listOf("Reptile", "Dragon"),
                attributes = listOf("Vaccine", "Data"),
                description = "First English description",
            ),
            result,
        )
    }

    @Test
    fun `GIVEN empty detail collections, WHEN toDomain is called, THEN uses empty defaults`() {
        // GIVEN
        val response = DigimonDetailDTO("Agumon", emptyList(), emptyList(), emptyList(), emptyList(), emptyList())

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals(
            DigimonDetailModel("Agumon", null, "", emptyList(), emptyList(), ""),
            result,
        )
    }

    @Test
    fun `GIVEN no English description, WHEN toDomain is called, THEN returns an empty description`() {
        // GIVEN
        val response = DigimonDetailDTO(
            "Agumon", emptyList(), emptyList(), emptyList(), emptyList(),
            listOf(DigimonDescriptionDTO("ja_jp", "Japanese description")),
        )

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals("", result.description)
    }
}

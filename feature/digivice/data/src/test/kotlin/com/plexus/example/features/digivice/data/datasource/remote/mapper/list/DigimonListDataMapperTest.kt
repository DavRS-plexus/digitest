package com.plexus.example.features.digivice.data.datasource.remote.mapper.list

import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.PaginationDTO
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DigimonListDataMapperTest {
    private val pageable = PaginationDTO(
        currentPage = 0,
        elementsOnPage = 2,
        totalElements = 2,
        totalPages = 1,
        previousPage = "",
        nextPage = ""
    )

    @Test
    fun `GIVEN a list of digimons, WHEN toDomain is called, THEN maps all fields properly`() {
        // GIVEN
        val first = DigimonDTO(1, "Agumon", "unused", "https://example.com/agumon.png")
        val second = DigimonDTO(2, "Airdramon", "unused", "https://example.com/airdramon.png")
        val response = DigimonListDTO(listOf(first, second), pageable)

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals(
            DigimonListDomainModel(
                listOf(
                    DigimonOverviewDomainModel("1", "Agumon", "https://example.com/agumon.png"),
                    DigimonOverviewDomainModel("2", "Airdramon", "https://example.com/airdramon.png"),
                )
            ),
            result
        )
    }

    @Test
    fun `GIVEN empty content, WHEN toDomain is called, THEN returns an empty list`() {
        // GIVEN
        val emptyList = DigimonListDTO(emptyList(), pageable)

        // WHEN
        val result = emptyList.toDomain()

        // THEN
        assertEquals(
            DigimonListDomainModel(emptyList()),
            result
        )
    }

    @Test
    fun `GIVEN malformed and valid items, WHEN toDomain is called, THEN skips the malformed item and preserves valid items`() {
        // GIVEN
        val malformed = mockk<DigimonDTO>()
        every { malformed.id } throws IllegalStateException("Invalid id")
        val valid = DigimonDTO(3, "Angemon", "unused", "image")
        val response = DigimonListDTO(listOf(malformed, valid), pageable)

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals(
            DigimonListDomainModel(
                listOf(DigimonOverviewDomainModel("3", "Angemon", "image"))),
            result
        )
    }
}

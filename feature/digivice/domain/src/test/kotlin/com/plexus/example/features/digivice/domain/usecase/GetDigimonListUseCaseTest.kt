package com.plexus.example.features.digivice.domain.usecase

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import com.plexus.example.libs.kotlinutils.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetDigimonListUseCaseTest {
    private val repository = mockk<DigimonRepository>()
    private lateinit var useCase: GetDigimonListUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetDigimonListUseCase(repository)
    }

    @Test
    fun `GIVEN a successful repository result, WHEN invoked, THEN emits the digimon list`() = runTest {
        // GIVEN
        val digimons = listOf(DigimonOverviewDomainModel("1", "Agumon", "image"))
        coEvery { repository.getDigimonList() } returns flowOf(Either.Success(digimons))

        // WHEN
        val result = useCase().toList()

        // THEN
        assertEquals(listOf(Either.Success(digimons)), result)
        coVerify(exactly = 1) { repository.getDigimonList() }
    }

    @Test
    fun `GIVEN a repository failure, WHEN invoked, THEN emits the same error`() = runTest {
        // GIVEN
        val error = ErrorDomainModel(500, "Request failed")
        coEvery { repository.getDigimonList() } returns flowOf(Either.Failure(error))

        // WHEN
        val result = useCase().toList()

        // THEN
        assertEquals(listOf(Either.Failure(error)), result)
        coVerify(exactly = 1) { repository.getDigimonList() }
    }

    @Test
    fun `GIVEN multiple repository emissions, WHEN invoked, THEN emits them in order`() = runTest {
        // GIVEN
        val first = listOf(DigimonOverviewDomainModel("1", "Agumon", "first"))
        val second = listOf(DigimonOverviewDomainModel("2", "Gabumon", "second"))
        coEvery { repository.getDigimonList() } returns flowOf(Either.Success(first), Either.Success(second))

        // WHEN
        val result = useCase().toList()

        // THEN
        assertEquals(listOf(Either.Success(first), Either.Success(second)), result)
        coVerify(exactly = 1) { repository.getDigimonList() }
    }

    @Test
    fun `GIVEN a repository exception, WHEN invoked, THEN propagates the exception`() {
        // GIVEN
        val exception = IOException("Repository unavailable")
        coEvery { repository.getDigimonList() } throws exception

        // WHEN
        val result = assertThrows(IOException::class.java) {
            runTest { useCase() }
        }

        // THEN
        assertSame(exception, result)
        coVerify(exactly = 1) { repository.getDigimonList() }
    }
}

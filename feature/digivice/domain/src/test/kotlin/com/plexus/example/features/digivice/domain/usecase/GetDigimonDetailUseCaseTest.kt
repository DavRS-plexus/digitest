package com.plexus.example.features.digivice.domain.usecase

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import com.plexus.example.libs.kotlinutils.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetDigimonDetailUseCaseTest {
    private val repository = mockk<DigimonRepository>()
    private lateinit var useCase: GetDigimonDetailUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetDigimonDetailUseCase(repository)
    }

    @Test
    fun `GIVEN a successful repository result, WHEN invoked with an id, THEN emits the digimon detail`() = runTest {
        // GIVEN
        val detail = DigimonDetailModel("Agumon", "image", "Rookie", listOf("Reptile"), listOf("Vaccine"), "Description")
        coEvery { repository.getDigimonDetail("1") } returns flowOf(Either.Success(detail))

        // WHEN
        val result = useCase("1").single()

        // THEN
        assertEquals(Either.Success(detail), result)
        coVerify(exactly = 1) { repository.getDigimonDetail("1") }
    }

    @Test
    fun `GIVEN a repository failure, WHEN invoked with an id, THEN emits the same error`() = runTest {
        // GIVEN
        val error = ErrorDomainModel(404, "Not found")
        coEvery { repository.getDigimonDetail("2") } returns flowOf(Either.Failure(error))

        // WHEN
        val result = useCase("2").single()

        // THEN
        assertEquals(Either.Failure(error), result)
        coVerify(exactly = 1) { repository.getDigimonDetail("2") }
    }

    @Test
    fun `GIVEN multiple repository emissions, WHEN invoked with an id, THEN emits them in order`() = runTest {
        // GIVEN
        val first = DigimonDetailModel("Agumon", null, "Rookie", emptyList(), emptyList(), "")
        val second = DigimonDetailModel("Greymon", null, "Champion", emptyList(), emptyList(), "")
        coEvery { repository.getDigimonDetail("1") } returns flowOf(
            Either.Success(first), Either.Success(second)
        )

        // WHEN
        val result = useCase("1").toList()

        // THEN
        assertEquals(listOf(Either.Success(first), Either.Success(second)), result)
        coVerify(exactly = 1) { repository.getDigimonDetail("1") }
    }

    @Test
    fun `GIVEN a repository exception, WHEN invoked with an id, THEN propagates the exception`() {
        // GIVEN
        val exception = IOException("Repository unavailable")
        coEvery { repository.getDigimonDetail("1") } throws exception

        // WHEN
        val result = assertThrows(IOException::class.java) {
            runTest { useCase("1") }
        }

        // THEN
        assertSame(exception, result)
        coVerify(exactly = 1) { repository.getDigimonDetail("1") }
    }
}

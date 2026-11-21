package com.plexus.example.features.digivice.data.datasource.remote

import com.plexus.example.core.error.data.datasource.remote.model.EmptyBodyErrorResponseDTO
import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.features.digivice.data.datasource.remote.api.DigimonApiService
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.PaginationDTO
import com.plexus.example.libs.kotlinutils.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class DigimonRemoteDataSourceImplTest {
    private val apiService = mockk<DigimonApiService>()

    private lateinit var dataSource: DigimonRemoteDataSourceImpl

    private val list = DigimonListDTO(
        listOf(DigimonDTO(1, "Agumon", "href", "image")),
        PaginationDTO(0, 1, 1, 1, "", ""),
    )
    private val detail = DigimonDetailDTO(
        "Agumon", emptyList(), emptyList(), emptyList(), emptyList(), emptyList(),
    )

    @BeforeEach
    fun setUp() {
        dataSource = DigimonRemoteDataSourceImpl(apiService)
    }

    @AfterEach
    fun tearDown() {

    }

    @Test
    fun `GIVEN a successful list response, WHEN getDigimonList is called, THEN returns the data`() = runTest {
        // GIVEN
        coEvery { apiService.getDigimons(20) } returns Response.success(list)

        // WHEN
        val result = dataSource.getDigimonList()

        // THEN
        assertEquals(Either.Success(list), result)
        coVerify(exactly = 1) { apiService.getDigimons(20) }
    }

    @Test
    fun `GIVEN an empty successful list response, WHEN getDigimonList is called, THEN returns an empty body error`() = runTest {
        // GIVEN
        coEvery { apiService.getDigimons(20) } returns Response.success<DigimonListDTO>(204, null)

        // WHEN
        val result = dataSource.getDigimonList()

        // THEN
        assertEquals(Either.Failure(EmptyBodyErrorResponseDTO(204)), result)
        coVerify(exactly = 1) { apiService.getDigimons(20) }
    }

    @Test
    fun `GIVEN an unsuccessful list response, WHEN getDigimonList is called, THEN returns the parsed error`() = runTest {
        // GIVEN
        coEvery { apiService.getDigimons(20) } returns errorResponse()

        // WHEN
        val result = dataSource.getDigimonList()

        // THEN
        assertParsedError(result)
        coVerify(exactly = 1) { apiService.getDigimons(20) }
    }

    @Test
    fun `GIVEN a successful search response, WHEN searchDigimon is called, THEN returns the body`() = runTest {
        // GIVEN
        coEvery { apiService.searchDigimon(20, "Agumon") } returns Response.success(list)

        // WHEN
        val result = dataSource.searchDigimon("Agumon")

        // THEN
        assertEquals(Either.Success(list), result)
        coVerify(exactly = 1) { apiService.searchDigimon(20, "Agumon") }
    }

    @Test
    fun `GIVEN an empty successful search response, WHEN searchDigimon is called, THEN returns an empty body error`() = runTest {
        // GIVEN
        coEvery { apiService.searchDigimon(20, "Agumon") } returns Response.success<DigimonListDTO>(204, null)

        // WHEN
        val result = dataSource.searchDigimon("Agumon")

        // THEN
        assertEquals(Either.Failure(EmptyBodyErrorResponseDTO(204)), result)
        coVerify(exactly = 1) { apiService.searchDigimon(20, "Agumon") }
    }

    @Test
    fun `GIVEN an unsuccessful search response, WHEN searchDigimon is called, THEN returns the parsed error`() = runTest {
        // GIVEN
        coEvery { apiService.searchDigimon(20, "Agumon") } returns errorResponse()

        // WHEN
        val result = dataSource.searchDigimon("Agumon")

        // THEN
        assertParsedError(result)
        coVerify(exactly = 1) { apiService.searchDigimon(20, "Agumon") }
    }

    @Test
    fun `GIVEN a successful detail response, WHEN getDigimonDetail is called, THEN returns the body`() = runTest {
        // GIVEN
        coEvery { apiService.getDigimonDetail("1") } returns Response.success(detail)

        // WHEN
        val result = dataSource.getDigimonDetail("1")

        // THEN
        assertEquals(Either.Success(detail), result)
        coVerify(exactly = 1) { apiService.getDigimonDetail("1") }
    }

    @Test
    fun `GIVEN an empty successful detail response, WHEN getDigimonDetail is called, THEN returns an empty body error`() = runTest {
        // GIVEN
        coEvery { apiService.getDigimonDetail("1") } returns Response.success<DigimonDetailDTO>(204, null)

        // WHEN
        val result = dataSource.getDigimonDetail("1")

        // THEN
        assertEquals(Either.Failure(EmptyBodyErrorResponseDTO(204)), result)
        coVerify(exactly = 1) { apiService.getDigimonDetail("1") }
    }

    @Test
    fun `GIVEN an unsuccessful detail response, WHEN getDigimonDetail is called, THEN returns the parsed error`() = runTest {
        // GIVEN
        coEvery { apiService.getDigimonDetail("1") } returns errorResponse()

        // WHEN
        val result = dataSource.getDigimonDetail("1")

        // THEN
        assertParsedError(result)
        coVerify(exactly = 1) { apiService.getDigimonDetail("1") }
    }

    @Test
    fun `GIVEN an API exception, WHEN getDigimonList is called, THEN propagates the exception`() {
        // GIVEN
        val exception = IOException("Network error")
        coEvery { apiService.getDigimons(20) } throws exception

        // WHEN
        val result = assertThrows(IOException::class.java) {
            runTest { dataSource.getDigimonList() }
        }

        // THEN
        assertSame(exception, result)
        coVerify(exactly = 1) { apiService.getDigimons(20) }
    }

    private fun <T> errorResponse(): Response<T> =
        Response.error(404, """{"error":42,"message":"Not found"}""".toResponseBody())

    private fun assertParsedError(result: Either<*, ErrorResponseDTO>) {
        val failure = assertInstanceOf(Either.Failure::class.java, result)
        val error = assertInstanceOf(ErrorResponseDTO::class.java, failure.fail)
        assertEquals(404, error.httpCode)
        assertEquals("""{"error":42,"message":"Not found"}""", error.response)
        assertEquals(42, error.error?.error)
        assertEquals("Not found", error.error?.message)
    }
}

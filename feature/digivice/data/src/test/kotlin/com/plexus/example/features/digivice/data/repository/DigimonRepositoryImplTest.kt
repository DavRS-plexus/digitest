package com.plexus.example.features.digivice.data.repository

import com.plexus.example.core.error.data.datasource.remote.model.ErrorDTO
import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.data.datasource.local.DigimonLocalDataSource
import com.plexus.example.features.digivice.data.datasource.local.model.DigimonOverviewDataModel
import com.plexus.example.features.digivice.data.datasource.local.model.DigimonSearchDataModel
import com.plexus.example.features.digivice.data.datasource.remote.DigimonRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonAttributeDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDescriptionDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonImageDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonLevelDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonTypeDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.PaginationDTO
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.libs.kotlinutils.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException


class DigimonRepositoryImplTest {
    private val remote = mockk<DigimonRemoteDataSource>()
    private val local = mockk<DigimonLocalDataSource>()
    private lateinit var repository: DigimonRepositoryImpl

    private val listDto = DigimonListDTO(
        listOf(DigimonDTO(1, "Agumon", "href", "image")),
        PaginationDTO(0, 1, 1, 1, "", ""),
    )
    private val overview = DigimonOverviewDomainModel("1", "Agumon", "image")
    private val cachedList =
        DigimonSearchDataModel(listOf(DigimonOverviewDataModel("1", "Agumon", "image")))
    private val errorResponse =
        ErrorResponseDTO(error = ErrorDTO(42, "Request failed"), httpCode = 404)

    @BeforeEach
    fun setUp() {
        repository = DigimonRepositoryImpl(remote, local)
    }

    @Test
    fun `GIVEN a successful list response, WHEN collecting the list, THEN emits mapped digimons`() =
        runTest {
            // GIVEN
            coEvery { remote.getDigimonList() } returns Either.Success(listDto)

            // WHEN
            val result = repository.getDigimonList().single()

            // THEN
            assertEquals(Either.Success(listOf(overview)), result)
            coVerify(exactly = 1) { remote.getDigimonList() }
        }

    @Test
    fun `GIVEN a failed list response, WHEN collecting the list, THEN emits the mapped error`() =
        runTest {
            // GIVEN
            coEvery { remote.getDigimonList() } returns Either.Failure(errorResponse)

            // WHEN
            val result = repository.getDigimonList().single()

            // THEN
            assertFailure(result, 42, "Request failed")
            coVerify(exactly = 1) { remote.getDigimonList() }
        }

    @Test
    fun `GIVEN a list exception, WHEN collecting the list, THEN emits an exception error`() =
        runTest {
            // GIVEN
            coEvery { remote.getDigimonList() } throws IOException("List unavailable")

            // WHEN
            val result = repository.getDigimonList().single()

            // THEN
            assertFailure(result, ErrorDomainModel.ERROR_EXCEPTION_CODE, "List unavailable")
            coVerify(exactly = 1) { remote.getDigimonList() }
        }

    @Test
    fun `GIVEN a cached search, WHEN collecting results, THEN emits cached data without a remote request`() =
        runTest {
            // GIVEN
            coEvery { local.searchDigimon("Agumon") } returns cachedList

            // WHEN
            val result = repository.searchDigimon("Agumon").single()

            // THEN
            assertEquals(Either.Success(DigimonListDomainModel(listOf(overview))), result)
            coVerify(exactly = 1) { local.searchDigimon("Agumon") }
            coVerify(exactly = 0) { remote.searchDigimon(any()) }
            coVerify(exactly = 0) { local.saveDigimonList(any(), any()) }
        }

    @Test
    fun `GIVEN a cache miss and successful response, WHEN collecting results, THEN emits and saves mapped data`() =
        runTest {
            // GIVEN
            coEvery { local.searchDigimon("Agumon") } returns null
            coEvery { remote.searchDigimon("Agumon") } returns Either.Success(listDto)
            coEvery { local.saveDigimonList("Agumon", cachedList) } returns Unit

            // WHEN
            val result = repository.searchDigimon("Agumon").single()

            // THEN
            assertEquals(Either.Success(DigimonListDomainModel(listOf(overview))), result)
            coVerify(exactly = 1) { local.searchDigimon("Agumon") }
            coVerify(exactly = 1) { remote.searchDigimon("Agumon") }
            coVerify(exactly = 1) { local.saveDigimonList("Agumon", cachedList) }
        }

    @Test
    fun `GIVEN a cache miss and failed response, WHEN collecting results, THEN emits the error without saving`() =
        runTest {
            // GIVEN
            coEvery { local.searchDigimon("Agumon") } returns null
            coEvery { remote.searchDigimon("Agumon") } returns Either.Failure(errorResponse)

            // WHEN
            val result = repository.searchDigimon("Agumon").single()

            // THEN
            assertFailure(result, 42, "Request failed")
            coVerify(exactly = 1) { local.searchDigimon("Agumon") }
            coVerify(exactly = 1) { remote.searchDigimon("Agumon") }
            coVerify(exactly = 0) { local.saveDigimonList(any(), any()) }
        }

    @Test
    fun `GIVEN a remote search exception, WHEN collecting results, THEN emits an exception error without saving`() =
        runTest {
            // GIVEN
            coEvery { local.searchDigimon("Agumon") } returns null
            coEvery { remote.searchDigimon("Agumon") } throws IOException("Search unavailable")

            // WHEN
            val result = repository.searchDigimon("Agumon").single()

            // THEN
            assertFailure(result, ErrorDomainModel.ERROR_EXCEPTION_CODE, "Search unavailable")
            coVerify(exactly = 1) { remote.searchDigimon("Agumon") }
            coVerify(exactly = 0) { local.saveDigimonList(any(), any()) }
        }

    @Test
    fun `GIVEN a successful detail response, WHEN collecting the detail, THEN emits the mapped model`() =
        runTest {
            // GIVEN
            val detail = DigimonDetailDTO(
                "Agumon", listOf(DigimonImageDTO("image")), listOf(DigimonLevelDTO("Rookie")),
                listOf(DigimonTypeDTO("Reptile")), listOf(DigimonAttributeDTO("Vaccine")),
                listOf(DigimonDescriptionDTO("en_us", "Description")),
            )
            coEvery { remote.getDigimonDetail("1") } returns Either.Success(detail)

            // WHEN
            val result = repository.getDigimonDetail("1").single()

            // THEN
            assertEquals(
                Either.Success(
                    DigimonDetailModel(
                        "Agumon",
                        "image",
                        "Rookie",
                        listOf("Reptile"),
                        listOf("Vaccine"),
                        "Description"
                    )
                ),
                result,
            )
            coVerify(exactly = 1) { remote.getDigimonDetail("1") }
        }

    @Test
    fun `GIVEN a failed detail response, WHEN collecting the detail, THEN emits the mapped error`() =
        runTest {
            // GIVEN
            coEvery { remote.getDigimonDetail("1") } returns Either.Failure(errorResponse)

            // WHEN
            val result = repository.getDigimonDetail("1").single()

            // THEN
            assertFailure(result, 42, "Request failed")
            coVerify(exactly = 1) { remote.getDigimonDetail("1") }
        }

    @Test
    fun `GIVEN a detail exception, WHEN collecting the detail, THEN emits an exception error`() =
        runTest {
            // GIVEN
            coEvery { remote.getDigimonDetail("1") } throws IOException("Detail unavailable")

            // WHEN
            val result = repository.getDigimonDetail("1").single()

            // THEN
            assertFailure(result, ErrorDomainModel.ERROR_EXCEPTION_CODE, "Detail unavailable")
            coVerify(exactly = 1) { remote.getDigimonDetail("1") }
        }

    private fun assertFailure(result: Either<*, ErrorDomainModel>, code: Int, message: String) {
        val failure = assertInstanceOf(Either.Failure::class.java, result)
        val error = assertInstanceOf(ErrorDomainModel::class.java, failure.fail)
        assertEquals(code, error.code)
        assertEquals(message, error.message)
    }
}

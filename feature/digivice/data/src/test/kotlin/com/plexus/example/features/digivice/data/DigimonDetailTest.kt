package com.plexus.example.features.digivice.data

import com.google.gson.Gson
import com.plexus.example.core.storage.data.datasource.local.LocalStorageManagerImpl
import com.plexus.example.features.digivice.data.datasource.local.DigimonLocalDataSourceImpl
import com.plexus.example.features.digivice.data.datasource.remote.DigimonRemoteDataSourceImpl
import com.plexus.example.features.digivice.data.datasource.remote.api.DigimonApiService
import com.plexus.example.features.digivice.data.datasource.remote.mapper.detail.toDomain
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.repository.DigimonRepositoryImpl
import com.plexus.example.features.digivice.domain.usecase.GetDigimonDetailUseCase
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class DigimonDetailTest {
    private val dto = Gson().fromJson(
        """
        {
            "id": 1,
            "name": "Agumon",
            "images": [{"href": "https://example.com/agumon.png", "transparent": false}],
            "levels": [{"id": 4, "level": "Child"}, {"id": 5, "level": "Adult"}],
            "types": [{"id": 1, "type": "Reptile"}],
            "attributes": [{"id": 4, "attribute": "Vaccine"}],
            "descriptions": [
                {"language": "jap", "description": "Japanese"},
                {"language": "en_us", "description": "English"},
                {"language": "en_us", "description": "Another description"}
            ]
        }
        """.trimIndent(),
        DigimonDetailDTO::class.java,
    )

    @Test
    fun `maps all requested fields and only English descriptions`() {
        val detail = dto.toDomain()

        assertEquals("Agumon", detail.name)
        assertEquals("https://example.com/agumon.png", detail.imageUrl)
        assertEquals(listOf("Child", "Adult"), detail.levels)
        assertEquals(listOf("Reptile"), detail.types)
        assertEquals(listOf("Vaccine"), detail.attributes)
        assertEquals(listOf("English", "Another description"), detail.descriptions)
    }

    @Test
    fun `handles empty optional collections`() {
        val detail = dto.copy(
            images = emptyList(),
            levels = emptyList(),
            types = emptyList(),
            attributes = emptyList(),
            descriptions = emptyList(),
        ).toDomain()

        assertNull(detail.imageUrl)
        assertEquals(emptyList<String>(), detail.descriptions)
        assertEquals(emptyList<String>(), detail.levels)
    }

    @Test
    fun `loads the requested id through the use case and repository`() = runBlocking {
        val api = FakeApi(Response.success(dto))
        val useCase = GetDigimonDetailUseCase(repository(api))

        val result = useCase("1").first()

        assertEquals("1", api.requestedId)
        assertEquals(Either.Success(dto.toDomain()), result)
    }

    @Test
    fun `propagates HTTP errors to the use case`() = runBlocking {
        val body = "{}".toResponseBody("application/json".toMediaType())
        val api = FakeApi(Response.error(404, body))
        val useCase = GetDigimonDetailUseCase(repository(api))

        assertEquals(Either.Failure("HTTP 404"), useCase("9999").first())
    }

    @Test
    fun `propagates empty response bodies`() = runBlocking {
        val api = FakeApi(Response.success(null))
        val useCase = GetDigimonDetailUseCase(repository(api))

        assertEquals(Either.Failure("Empty response body"), useCase("1").first())
    }

    @Test
    fun `propagates network failures`() = runBlocking {
        val api = object : DigimonApiService {
            override suspend fun getDigimons(pageSize: Int?, name: String?): Response<DigimonListDTO> =
                error("Unexpected list request")

            override suspend fun getDigimonDetail(id: String): Response<DigimonDetailDTO> =
                throw IOException("Network unavailable")
        }
        val useCase = GetDigimonDetailUseCase(repository(api))

        assertEquals(Either.Failure("Network unavailable"), useCase("1").first())
    }

    private fun repository(api: DigimonApiService) = DigimonRepositoryImpl(
        DigimonRemoteDataSourceImpl(api),
        DigimonLocalDataSourceImpl(LocalStorageManagerImpl()),
    )

    private class FakeApi(private val detailResponse: Response<DigimonDetailDTO>) : DigimonApiService {
        var requestedId: String? = null

        override suspend fun getDigimons(pageSize: Int?, name: String?): Response<DigimonListDTO> =
            error("Unexpected list request")

        override suspend fun getDigimonDetail(id: String): Response<DigimonDetailDTO> {
            requestedId = id
            return detailResponse
        }
    }
}

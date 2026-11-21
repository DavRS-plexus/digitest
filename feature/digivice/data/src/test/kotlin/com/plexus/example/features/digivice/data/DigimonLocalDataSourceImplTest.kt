package com.plexus.example.features.digivice.data

import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.storage.data.datasource.local.LocalStorageManagerImpl
import com.plexus.example.features.digivice.data.datasource.local.DigimonLocalDataSourceImpl
import com.plexus.example.features.digivice.data.datasource.remote.DigimonRemoteDataSource
import com.plexus.example.features.digivice.data.datasource.remote.model.response.detail.DigimonDetailDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.DigimonListDTO
import com.plexus.example.features.digivice.data.datasource.remote.model.response.list.PaginationDTO
import com.plexus.example.features.digivice.data.repository.DigimonRepositoryImpl
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class DigimonLocalDataSourceImplTest {
    private var now = 0L
    private val source = FakeRemoteDataSource()
    private val cache = DigimonLocalDataSourceImpl(LocalStorageManagerImpl()) { now }
    private val repository = DigimonRepositoryImpl(source, cache)

    @Test
    fun `successful searches are cached for less than five minutes`() = runBlocking {
        val first = repository.searchDigimon("agu").first()
        now = TimeUnit.MINUTES.toNanos(5) - 1
        assertEquals(first, repository.searchDigimon("agu").first())
        assertEquals(1, source.calls)

        now = TimeUnit.MINUTES.toNanos(5)
        val refreshed = repository.searchDigimon("agu").first()
        assertEquals(2, source.calls)
        assertTrue(first != refreshed)
        assertEquals(refreshed, repository.searchDigimon("agu").first())
        assertEquals(2, source.calls)
    }

    @Test
    fun `queries have separate entries including empty results`() = runBlocking {
        source.empty = true
        val first = repository.searchDigimon("agu").first()
        repository.searchDigimon("gabu").first()
        assertEquals(first, repository.searchDigimon("agu").first())
        assertEquals(2, source.calls)
    }

    @Test
    fun `failed responses are not cached`() = runBlocking {
        source.fail = true
        assertTrue(repository.searchDigimon("agu").first() is Either.Failure)
        source.fail = false
        assertTrue(repository.searchDigimon("agu").first() is Either.Success)
        assertEquals(2, source.calls)
    }

    @Test
    fun `expired success is not returned when refresh fails`() = runBlocking {
        repository.searchDigimon("agu").first()
        now = TimeUnit.MINUTES.toNanos(5)
        source.fail = true
        assertTrue(repository.searchDigimon("agu").first() is Either.Failure)
        source.fail = false
        assertTrue(repository.searchDigimon("agu").first() is Either.Success)
        assertEquals(3, source.calls)
    }

    private class FakeRemoteDataSource : DigimonRemoteDataSource {
        var calls = 0
        var fail = false
        var empty = false

        override suspend fun searchDigimon(name: String): Either<DigimonListDTO, String> {
            calls++
            if (fail) return Either.failure("Network failure")
            return Either.success(
                DigimonListDTO(
                    content = if (empty) emptyList() else listOf(DigimonDTO(calls, name, "", "image")),
                    pageable = PaginationDTO(0, 1, 1, 1, "", ""),
                )
            )
        }

        override suspend fun getDigimonList(): Either<DigimonListDTO, String> =
            error("Unexpected list request")

        override suspend fun getDigimonDetail(id: String): Either<DigimonDetailDTO, ErrorResponseDTO> =
            error("Unexpected detail request")
    }
}

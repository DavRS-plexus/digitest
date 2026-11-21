package com.plexus.example.core.error.data.datasource.remote.mapper

import com.plexus.example.core.error.data.datasource.remote.model.ErrorDTO
import com.plexus.example.core.error.data.datasource.remote.model.ErrorResponseDTO
import com.plexus.example.core.error.domain.model.ErrorDomainModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ErrorMapperTest {
    @Test
    fun `GIVEN an error response, WHEN toDomain is called, THEN maps its code and message`() {
        // GIVEN
        val response = ErrorResponseDTO(error = ErrorDTO(42, "Request failed"), httpCode = 400)

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals(42, result.code)
        assertEquals("Request failed", result.message)
    }

    @Test
    fun `GIVEN a response without an error, WHEN toDomain is called, THEN uses unknown error defaults`() {
        // GIVEN
        val response = ErrorResponseDTO(httpCode = 500)

        // WHEN
        val result = response.toDomain()

        // THEN
        assertEquals(ErrorDomainModel.ERROR_UNKNOWN_CODE, result.code)
        assertEquals(ErrorDomainModel.ERROR_UNKNOWN_MESSAGE, result.message)
    }
}

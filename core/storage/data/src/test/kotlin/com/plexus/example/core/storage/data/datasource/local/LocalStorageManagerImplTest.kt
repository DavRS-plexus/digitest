package com.plexus.example.core.storage.data.datasource.local

import com.plexus.example.core.storage.data.datasource.local.datastore.runtime.RuntimeDataStore
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalStorageManagerImplTest {
    private val dataStore = mockk<RuntimeDataStore>()
    private lateinit var manager: LocalStorageManagerImpl

    @BeforeEach
    fun setUp() {
        manager = LocalStorageManagerImpl(dataStore)
    }

    @Test
    fun `GIVEN a value and expiration, WHEN save is called, THEN the value is saved`() {
        // GIVEN
        val value = Any()
        every { dataStore.save("key", value, 123L, any()) } returns Unit

        // WHEN
        manager.save("key", value, 123L)

        // THEN
        verify(exactly = 1) { dataStore.save("key", value, 123L, any()) }
    }

    @Test
    fun `GIVEN a stored value, WHEN get is called, THEN returns the value from the data store`() {
        // GIVEN
        val value = Any()
        every { dataStore.get("key", Any::class.java) } returns value

        // WHEN
        val result = manager.get("key", Any::class.java)

        // THEN
        assertSame(value, result)
        verify(exactly = 1) { dataStore.get("key", Any::class.java) }
    }

    @Test
    fun `GIVEN a missing value, WHEN get is called, THEN returns null`() {
        // GIVEN
        every { dataStore.get("missing", String::class.java) } returns null

        // WHEN
        val result = manager.get("missing", String::class.java)

        // THEN
        assertNull(result)
        verify(exactly = 1) { dataStore.get("missing", String::class.java) }
    }

    @Test
    fun `GIVEN a data store type error, WHEN get is called, THEN propagates the exception`() {
        // GIVEN
        val exception = ClassCastException("Incorrect type")
        every { dataStore.get("key", String::class.java) } throws exception

        // WHEN
        val result = assertThrows(ClassCastException::class.java) {
            manager.get("key", String::class.java)
        }

        // THEN
        assertSame(exception, result)
        verify(exactly = 1) { dataStore.get("key", String::class.java) }
    }
}

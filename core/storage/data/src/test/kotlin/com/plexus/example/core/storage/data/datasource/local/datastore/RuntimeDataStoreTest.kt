package com.plexus.example.core.storage.data.datasource.local.datastore

import com.plexus.example.core.storage.data.datasource.local.datastore.runtime.RuntimeDataStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RuntimeDataStoreTest {
    private lateinit var dataStore: RuntimeDataStore

    @BeforeEach
    fun setUp() {
        dataStore = RuntimeDataStore()
    }

    @Test
    fun `GIVEN a saved value, WHEN get is called before expiration, THEN returns the value`() {
        // GIVEN
        val value = Any()
        dataStore.save("key", value, Long.MAX_VALUE)

        // WHEN
        val result = dataStore.get("key", Any::class.java)

        // THEN
        assertSame(value, result)
    }

    @Test
    fun `GIVEN a missing key, WHEN get is called, THEN returns null`() {
        // WHEN
        val result = dataStore.get("missing", String::class.java)

        // THEN
        assertNull(result)
    }

    @Test
    fun `GIVEN an expired value, WHEN get is called, THEN returns null`() {
        // GIVEN
        dataStore.save("key", "expired", -1L)

        // WHEN
        val result = dataStore.get("key", String::class.java)

        // THEN
        assertNull(result)
    }

    @Test
    fun `GIVEN a saved key, WHEN saving a replacement, THEN returns the new value`() {
        // GIVEN
        dataStore.save("key", "old", Long.MAX_VALUE)

        // WHEN
        dataStore.save("key", "new", Long.MAX_VALUE)
        val result = dataStore.get("key", String::class.java)

        // THEN
        assertEquals("new", result)
    }

    @Test
    fun `GIVEN multiple keys, WHEN one value expires, THEN the other value remains available`() {
        // GIVEN
        dataStore.save("expired", "old", -1L)
        dataStore.save("valid", "new", Long.MAX_VALUE)

        // WHEN
        val expired = dataStore.get("expired", String::class.java)
        val valid = dataStore.get("valid", String::class.java)

        // THEN
        assertNull(expired)
        assertEquals("new", valid)
    }

    @Test
    fun `GIVEN a value of another type, WHEN get is called, THEN throws a class cast exception`() {
        // GIVEN
        dataStore.save("key", "value", Long.MAX_VALUE)

        // WHEN
        val result = assertThrows(ClassCastException::class.java) {
            dataStore.get("key", Int::class.javaObjectType)
        }

        // THEN
        assertEquals(ClassCastException::class.java, result.javaClass)
        assertEquals("value", dataStore.get("key", String::class.java))
    }

    @Test
    fun `GIVEN an expired value of another type, WHEN get is called, THEN returns null before casting`() {
        // GIVEN
        dataStore.save("key", "expired", -1L)

        // WHEN
        val result = dataStore.get("key", Int::class.javaObjectType)

        // THEN
        assertNull(result)
    }
}

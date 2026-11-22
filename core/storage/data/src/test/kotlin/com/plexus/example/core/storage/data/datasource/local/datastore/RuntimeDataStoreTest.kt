package com.plexus.example.core.storage.data.datasource.local.datastore

import com.plexus.example.core.storage.data.datasource.local.datastore.runtime.RuntimeDataStore
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
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
        dataStore.save(
            key = "key",
            value = "expired",
            expirationTimeNanos = TimeUnit.SECONDS.toNanos(1),
            savingTimeNanos = System.nanoTime() - TimeUnit.MINUTES.toNanos(1),
        )

        // WHEN
        val result = dataStore.get("key", String::class.java)

        // THEN
        assertNull(result)
    }

    @Test
    fun `GIVEN an explicit saving time within expiration, WHEN get is called, THEN returns the value`() {
        // GIVEN
        dataStore.save(
            key = "key",
            value = "value",
            expirationTimeNanos = TimeUnit.HOURS.toNanos(1),
            savingTimeNanos = System.nanoTime() - TimeUnit.MINUTES.toNanos(1),
        )

        // WHEN
        val result = dataStore.get("key", String::class.java)

        // THEN
        assertEquals("value", result)
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

    @Test
    fun `GIVEN fifty entries, WHEN saving another entry, THEN removes the entry that was accessed least recently`() {
        // GIVEN
        repeat(50) { dataStore.save("key$it", "value$it", Long.MAX_VALUE) }
        assertEquals("value0", dataStore.get("key0", String::class.java))

        // WHEN
        dataStore.save("new", "new value", Long.MAX_VALUE)

        // THEN
        assertNull(dataStore.get("key1", String::class.java))
        assertEquals("value0", dataStore.get("key0", String::class.java))
        assertEquals("new value", dataStore.get("new", String::class.java))
        assertEquals(50, dataStore.size)
    }

    @Test
    fun `GIVEN a full cache, WHEN replacing a key, THEN retains all fifty entries`() {
        // GIVEN
        repeat(50) { dataStore.save("key$it", "value$it", Long.MAX_VALUE) }

        // WHEN
        dataStore.save("key0", "replacement", Long.MAX_VALUE)

        // THEN
        assertEquals("replacement", dataStore.get("key0", String::class.java))
        assertEquals(50, dataStore.size)
    }

    @Test
    fun `GIVEN a full cache with an unread expired newest entry, WHEN saving, THEN removes the entry that was accessed least recently`() {
        // GIVEN
        repeat(49) { dataStore.save("key$it", "value$it", Long.MAX_VALUE) }
        dataStore.save("expired", "old", -1L)

        // WHEN
        dataStore.save("new", "new value", Long.MAX_VALUE)

        // THEN
        assertNull(dataStore.get("key0", String::class.java))
        assertEquals(50, dataStore.size)
        assertEquals("new value", dataStore.get("new", String::class.java))
        assertNull(dataStore.get("expired", String::class.java))
        assertEquals(49, dataStore.size)
    }

    @Test
    fun `GIVEN a full cache with an expired entry, WHEN reading it before saving, THEN removes it and retains valid entries`() {
        // GIVEN
        repeat(49) { dataStore.save("key$it", "value$it", Long.MAX_VALUE) }
        dataStore.save("expired", "old", -1L)

        // WHEN
        assertNull(dataStore.get("expired", String::class.java))
        dataStore.save("new", "new value", Long.MAX_VALUE)

        // THEN
        assertEquals(50, dataStore.size)
        assertEquals("new value", dataStore.get("new", String::class.java))
    }

    @Test
    fun `GIVEN a full cache, WHEN replacing the oldest entry and adding a key, THEN retains the replacement as recently used`() {
        // GIVEN
        repeat(50) { dataStore.save("key$it", "value$it", Long.MAX_VALUE) }

        // WHEN
        dataStore.save("key0", "replacement", Long.MAX_VALUE)
        dataStore.save("new", "new value", Long.MAX_VALUE)

        // THEN
        assertNull(dataStore.get("key1", String::class.java))
        assertEquals("replacement", dataStore.get("key0", String::class.java))
        assertEquals("new value", dataStore.get("new", String::class.java))
    }
}

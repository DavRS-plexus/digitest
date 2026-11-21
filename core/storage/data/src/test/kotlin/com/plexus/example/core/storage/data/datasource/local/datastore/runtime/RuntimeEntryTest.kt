package com.plexus.example.core.storage.data.datasource.local.datastore.runtime

import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RuntimeEntryTest {
    @Test
    fun `GIVEN an entry within its expiration period, WHEN checking expiration, THEN returns false`() {
        // GIVEN
        val entry = RuntimeEntry("value", System.nanoTime(), Long.MAX_VALUE)

        // WHEN
        val result = entry.isExpired()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN an entry older than its expiration period, WHEN checking expiration, THEN returns true`() {
        // GIVEN
        val entry = RuntimeEntry(
            data = "value",
            saveTimeNanos = (System.nanoTime() - TimeUnit.MINUTES.toNanos(1)),
            expirationTimeNanos = TimeUnit.SECONDS.toNanos(1)
        )

        // WHEN
        val result = entry.isExpired()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a negative expiration period, WHEN checking expiration, THEN returns true`() {
        // GIVEN
        val entry = RuntimeEntry("value", System.nanoTime(), -1L)

        // WHEN
        val result = entry.isExpired()

        // THEN
        assertTrue(result)
    }
}

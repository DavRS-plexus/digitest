package com.plexus.example.core.storage.data.datasource.local.datastore.runtime

import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RuntimeEntryTest {
    @Test
    fun `GIVEN an entry within its expiration period, WHEN checking expiration, THEN returns false`() {
        // GIVEN
        val entry = RuntimeEntry("value", 0L, TimeUnit.MINUTES.toNanos(5))

        // WHEN
        val result = entry.isExpired(TimeUnit.SECONDS.toNanos(299))

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN an entry older than its expiration period, WHEN checking expiration, THEN returns true`() {
        // GIVEN
        val entry = RuntimeEntry(
            data = "value",
            savingTimeNanos = 0L,
            expirationTimeNanos = TimeUnit.MINUTES.toNanos(5)
        )

        // WHEN
        val result = entry.isExpired(TimeUnit.SECONDS.toNanos(301))

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a negative expiration period, WHEN checking expiration, THEN returns true`() {
        // GIVEN
        val entry = RuntimeEntry("value", 0L, -1L)

        // WHEN
        val result = entry.isExpired(0L)

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a five minute entry, WHEN exactly five minutes have elapsed, THEN it is not expired`() {
        val entry = RuntimeEntry("value", 0L, TimeUnit.MINUTES.toNanos(5))

        assertFalse(entry.isExpired(TimeUnit.MINUTES.toNanos(5)))
    }
}

package com.plexus.example.core.storage.data.datasource.local.datastore.runtime

data class RuntimeEntry<T>(
    val data: T,
    val savingTimeNanos: Long,
    val expirationTimeNanos: Long,
) {
    fun isExpired() = (System.nanoTime() - savingTimeNanos) > expirationTimeNanos
}

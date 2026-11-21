package com.plexus.example.core.storage.data.datasource.local.datastore.runtime

data class RuntimeEntry<T>(
    val data: T,
    val saveTimeNanos: Long,
    val expireTimeNanos: Long,
) {
    fun isExpired() = (System.nanoTime() - saveTimeNanos) > expireTimeNanos
}
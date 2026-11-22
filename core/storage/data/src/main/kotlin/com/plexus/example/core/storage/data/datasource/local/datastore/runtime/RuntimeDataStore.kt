package com.plexus.example.core.storage.data.datasource.local.datastore.runtime

import java.util.Collections

class RuntimeDataStore {
    private companion object {
        const val MAX_ENTRIES = 50
        const val DEFAULT_LOAD_FACTOR = 0.75f
    }

    private val storageMap = Collections.synchronizedMap(
        object : LinkedHashMap<String, RuntimeEntry<Any>>(MAX_ENTRIES, DEFAULT_LOAD_FACTOR, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, RuntimeEntry<Any>>): Boolean =
                size > MAX_ENTRIES
        }
    )

    fun save(key: String, value: Any, expirationTimeNanos: Long, savingTimeNanos: Long = System.nanoTime()) {
        synchronized(storageMap) {
            storageMap[key] = RuntimeEntry(value, savingTimeNanos, expirationTimeNanos)
        }
    }

    fun <T : Any> get(key: String, type: Class<T>): T? = synchronized(storageMap) {
        val entry = storageMap[key] ?: return@synchronized null

        if (entry.isExpired()) {
            storageMap.remove(key, entry)
            null
        } else {
            type.cast(entry.data)
        }
    }

    internal val size: Int
        get() = storageMap.size
}

package com.plexus.example.core.storage.data.datasource.local.datastore.runtime

import java.util.concurrent.ConcurrentHashMap

class RuntimeDataStore {
    private val storageMap = ConcurrentHashMap<String, RuntimeEntry<Any>>()

    fun save(key: String, value: Any, expireTimeNanos: Long) {
        val entry = RuntimeEntry(value, System.nanoTime(), expireTimeNanos)
        storageMap[key] = entry
    }

    fun <T : Any> get(key: String, type: Class<T>): T? {
        val entry = storageMap[key] ?: return null

        return if (entry.isExpired()) {
            storageMap.remove(key, entry)
            null
        } else {
            type.cast(entry.data)
        }
    }
}

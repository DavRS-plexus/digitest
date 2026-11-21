package com.plexus.example.core.storage.data.datasource.local

interface LocalStorageManager {
    fun save(key: String, value: Any, expireTimeNanos: Long)
    fun <T : Any> get(key: String, type: Class<T>): T?
}

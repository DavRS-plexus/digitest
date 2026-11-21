package com.plexus.example.core.storage.data.datasource.local

import com.plexus.example.core.storage.data.datasource.local.datastore.RuntimeDataStore

class LocalStorageManagerImpl(
    private val dataStore: RuntimeDataStore
) : LocalStorageManager {

    override fun save(key: String, value: Any, expireTimeNanos: Long) = dataStore.save(key, value, expireTimeNanos)
    override fun <T : Any> get(key: String, type: Class<T>): T? = dataStore.get(key, type)
}
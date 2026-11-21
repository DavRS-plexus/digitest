package com.plexus.example.features.digivice.data.datasource.local

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.core.storage.data.datasource.local.LocalStorageManager
import com.plexus.example.features.digivice.data.datasource.local.model.DigimonSearchDataModel
import com.plexus.example.libs.kotlinutils.Either
import java.util.concurrent.TimeUnit

internal class DigimonLocalDataSourceImpl(
    private val localStorageManager: LocalStorageManager,
): DigimonLocalDataSource {

    private companion object {
        private const val SEARCH_KEY = "digivice.search"
        val expireTimeNanos = TimeUnit.MINUTES.toNanos(5)
    }

    override suspend fun searchDigimon(name: String): DigimonSearchDataModel? {
        return try {
            localStorageManager.get(key(name), DigimonSearchDataModel::class.java)
        } catch (_: IllegalStateException) {
            null
        }
    }

    override suspend fun saveDigimonList(name: String, data: DigimonSearchDataModel) {
        localStorageManager.save(key(name), data, expireTimeNanos)
    }

    private fun key(name: String) = "$SEARCH_KEY:$name"
}

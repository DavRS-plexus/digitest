package com.plexus.example.core.storage.data.di

import com.plexus.example.core.storage.data.datasource.local.LocalStorageManager
import com.plexus.example.core.storage.data.datasource.local.LocalStorageManagerImpl
import com.plexus.example.core.storage.data.datasource.local.datastore.RuntimeDataStore
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val storageDataModule = module {
    single<RuntimeDataStore>()
    single<LocalStorageManagerImpl>() bind LocalStorageManager::class
}

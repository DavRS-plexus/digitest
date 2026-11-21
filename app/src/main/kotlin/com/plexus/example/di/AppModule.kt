package com.plexus.example.di

import com.plexus.example.core.network.networkModule
import com.plexus.example.features.digivice.data.di.digiviceDataModule
import com.plexus.example.features.digivice.domain.di.digiviceDomainModule
import com.plexus.example.features.digivice.ui.di.digiviceUiModule
import org.koin.dsl.module


val appModule = module {
    includes(networkModule, digiviceUiModule, digiviceDataModule, digiviceDomainModule)
}
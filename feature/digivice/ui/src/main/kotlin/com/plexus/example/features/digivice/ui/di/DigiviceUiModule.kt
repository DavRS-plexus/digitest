package com.plexus.example.features.digivice.ui.di

import com.plexus.example.features.digivice.ui.search.viewmodel.DigiviceSearchViewModel
import com.plexus.example.features.digivice.ui.detail.viewmodel.DigimonDetailViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val digiviceUiModule = module {
    viewModel<DigiviceSearchViewModel>()
    viewModel<DigimonDetailViewModel>()
}
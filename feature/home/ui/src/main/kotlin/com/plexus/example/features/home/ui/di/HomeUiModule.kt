package com.plexus.example.features.home.ui.di

import com.plexus.example.features.home.ui.viewmodel.HomeViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val homeUiModule = module {
    viewModel<HomeViewModel>()
}

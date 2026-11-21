package com.plexus.example.features.digivice.domain.di

import com.plexus.example.features.digivice.domain.usecase.GetDigimonListUseCase
import com.plexus.example.features.digivice.domain.usecase.GetDigimonDetailUseCase
import com.plexus.example.features.digivice.domain.usecase.SearchDigimonUseCase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val digiviceDomainModule = module {
    factory<GetDigimonListUseCase>()
    factory<SearchDigimonUseCase>()
    factory<GetDigimonDetailUseCase>()
}

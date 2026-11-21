package com.plexus.example.features.digivice.domain.di

import com.plexus.example.features.digivice.domain.usecase.GetDigimonListUseCase
import com.plexus.example.features.digivice.domain.usecase.SearchDigimonUseCase
import org.koin.dsl.module

val digiviceDomainModule = module {
    factory { GetDigimonListUseCase(get()) }
    factory { SearchDigimonUseCase(get()) }
}

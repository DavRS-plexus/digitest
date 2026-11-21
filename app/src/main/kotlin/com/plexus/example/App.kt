package com.plexus.example

import android.app.Application
import com.plexus.example.core.network.networkModule
import com.plexus.example.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        println("DAVID = app.Oncreate")
        initKoin()
    }

    fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}

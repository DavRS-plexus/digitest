package com.plexus.example

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        println("DAVID = app.Oncreate")
    }
}

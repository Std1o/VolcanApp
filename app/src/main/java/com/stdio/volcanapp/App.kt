package com.stdio.volcanapp

import android.app.Application
import com.stdio.uploadimage.di.presentationModule
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(presentationModule)
        }
    }
}
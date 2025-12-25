package com.stdio.volcanapp

import android.app.Application
import com.stdio.data.di.dataModule
import com.stdio.domain.di.domainModule
import com.stdio.uploadimage.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            val modules = presentationModule + dataModule + domainModule
            modules(modules)
        }
    }
}
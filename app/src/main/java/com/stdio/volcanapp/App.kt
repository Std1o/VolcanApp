package com.stdio.volcanapp

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        println("TADA")
    }
}
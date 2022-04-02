package com.example.tmdbumbaapp.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TMDBApplication:Application() {

    companion object {
        @get:Synchronized
        lateinit var application: TMDBApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}
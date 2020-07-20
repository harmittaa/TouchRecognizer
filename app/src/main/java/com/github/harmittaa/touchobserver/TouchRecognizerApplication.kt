package com.github.harmittaa.touchobserver

import android.app.Application
import timber.log.Timber

class TouchRecognizerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
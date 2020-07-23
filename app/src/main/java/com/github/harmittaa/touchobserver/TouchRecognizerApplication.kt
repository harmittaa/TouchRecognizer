package com.github.harmittaa.touchobserver

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TouchRecognizerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@TouchRecognizerApplication)
            modules(listOf(viewModelModule))
        }

    }
}
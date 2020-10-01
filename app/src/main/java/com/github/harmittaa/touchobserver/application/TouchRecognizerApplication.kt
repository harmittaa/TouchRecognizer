package com.github.harmittaa.touchobserver.application

import android.app.Application
import com.github.harmittaa.touchobserver.di.authModule
import com.github.harmittaa.touchobserver.di.dataStoreModule
import com.github.harmittaa.touchobserver.di.firebaseModule
import com.github.harmittaa.touchobserver.di.logicModule
import com.github.harmittaa.touchobserver.di.repositoryModule
import com.github.harmittaa.touchobserver.di.viewModelModule
import com.github.harmittaa.touchobserver.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class TouchRecognizerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@TouchRecognizerApplication)
            modules(
                listOf(
                    viewModelModule,
                    authModule,
                    firebaseModule,
                    repositoryModule,
                    logicModule,
                    dataStoreModule
                )
            )
        }
    }
}

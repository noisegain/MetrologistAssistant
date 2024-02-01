package com.noisegain.metrologist_assistant

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream


@HiltAndroidApp
class MetrologistApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
package com.teddy.koreantextrecognitionbymlkit

import android.app.Application
import timber.log.Timber

class KoreanRecognitionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
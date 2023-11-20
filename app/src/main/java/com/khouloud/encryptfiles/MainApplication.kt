package com.khouloud.encryptfiles

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {

    private val TAG = MainApplication::class.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }
}
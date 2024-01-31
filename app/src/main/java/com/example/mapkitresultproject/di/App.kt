package com.example.mapkitresultproject.di

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App :Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("5d5d9d21-39f8-4a06-b86b-3288d9720ca8")
    }
}
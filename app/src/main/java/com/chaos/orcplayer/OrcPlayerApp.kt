package com.chaos.orcplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OrcPlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

package ch.sdp.vibester

import android.app.Application
import ch.sdp.vibester.database.AppPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VibesterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}
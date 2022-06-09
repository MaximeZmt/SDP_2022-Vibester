package ch.sdp.vibester.database

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder

/**
 * Share some values across the whole application
 */
object AppPreferences {
    private const val NAME = "Vibester"
    private const val MODE = Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun getStr(key: String): String? {
        return if (this::preferences.isInitialized) {
            Log.d(null, "########## preferences is initialized ##########")
            preferences.getString(key, "")
        } else {
            Log.d(null, "########## preferences is noooot initialized ##########")
            ""
        }
    }

    fun setStr(key: String, value: String){
        if (this::preferences.isInitialized) {
            preferences.edit().putString(key, value).apply()
        }
    }
}

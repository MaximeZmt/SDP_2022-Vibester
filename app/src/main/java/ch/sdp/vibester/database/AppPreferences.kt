package ch.sdp.vibester.database

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
            preferences.getString(key, "")
        } else {
            ""
        }
    }

    fun setStr(key: String, value: String){
        if (this::preferences.isInitialized) {
            preferences.edit().putString(key, value).apply()
        }
    }
}

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

    /**
     * Saves object into the Preferences.
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> setObject(`object`: T, key: String) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        preferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> getObject(key: String): T? {
        val value = preferences.getString(key, null)
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}

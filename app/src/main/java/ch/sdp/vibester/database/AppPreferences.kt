package ch.sdp.vibester.database

import android.content.Context
import android.content.SharedPreferences

/**
 * Share some values across the whole application
 */
object AppPreferences {
    private const val NAME = "Vibester"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation:(SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun getStr(key: String): String? {
       return preferences.getString(key, "")
    }

    fun setStr(key: String, value: String){
        preferences.edit {
            it.putString(key, value)
        }
    }
}

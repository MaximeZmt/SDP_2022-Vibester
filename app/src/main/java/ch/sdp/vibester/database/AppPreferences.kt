package ch.sdp.vibester.database

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "Vibester"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val GAME_MODE = "local_typing"
    private val GAME_GENRE = "BTS"

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

    var gameMode: String?
        get() = preferences.getString(GAME_MODE, "local_lyrics")
        set(value) = preferences.edit {
            it.putString(GAME_MODE, value)
        }

    var gameGenre: String?
        get() = preferences.getString(GAME_GENRE, "BTS")
        set(value) = preferences.edit {
            it.putString(GAME_GENRE, value)
        }
}

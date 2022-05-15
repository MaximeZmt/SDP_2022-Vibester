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

    // list of app specific preferences
    private const val GAME_MODE = "game_mode"
    private const val GAME_GENRE = "game_genre"

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
        get() = preferences.getString(GAME_MODE, "")
        set(value) = preferences.edit {
            it.putString(GAME_MODE, value)
        }

    var gameGenre: String?
        get() = preferences.getString(GAME_GENRE, "")
        set(value) = preferences.edit {
            it.putString(GAME_GENRE, value)
        }
}

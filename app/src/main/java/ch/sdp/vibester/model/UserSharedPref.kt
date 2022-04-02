package ch.sdp.vibester.model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ch.sdp.vibester.profile.UserProfile


class UserSharedPref private constructor() {
    companion object{
        val HANDLE = "handle"
        val USERNAME = "username"
        val IMAGE = "image"
        val EMAIL = "email"
        val TOTAL_GAMES = "totalGames"
        val BEST_SCORE = "bestScore"
        val CORRECT_SONGS = "correctSongs"
        val RANKING = "ranking"
                /*
                    https://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state#:~:text=When%20users%20log%20in%20to,direct%20to%20the%20login%20page.
                 */

        fun getSharedPreferences(ctx: Context): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        fun setUser(ctx: Context, user: UserProfile){
            val edit = getSharedPreferences(ctx)?.edit()
            if (edit != null) {
                edit.putString(HANDLE, user.handle)
                edit.putString(USERNAME, user.handle)
                edit.putString(IMAGE, user.handle)
                edit.putString(EMAIL, user.handle)
                edit.putInt(TOTAL_GAMES, user.totalGames)
                edit.putInt(BEST_SCORE, user.bestScore)
                edit.putInt(CORRECT_SONGS, user.correctSongs)
                edit.putInt(RANKING, user.ranking)
                edit.commit()
            }
        }
    }
}
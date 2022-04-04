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
                edit.putString(USERNAME, user.username)
                edit.putString(IMAGE, user.image)
                edit.putString(EMAIL, user.email)
                edit.putInt(TOTAL_GAMES, user.totalGames)
                edit.putInt(BEST_SCORE, user.bestScore)
                edit.putInt(CORRECT_SONGS, user.correctSongs)
                edit.putInt(RANKING, user.ranking)
                edit.commit()
            }
        }

        fun updateScore(ctx: Context, deltaTotal: Int = 0, deltaBest: Int = 0, deltaCorrect: Int = 0, deltaRanking: Int = 0){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                val current_total_games = sharedPref.getInt(TOTAL_GAMES, 0) + deltaTotal
                val current_best_score = sharedPref.getInt(BEST_SCORE, 0) + deltaBest
                val current_correct_song = sharedPref.getInt(CORRECT_SONGS, 0) + deltaCorrect
                val current_ranking = sharedPref.getInt(RANKING, 0) + deltaRanking
                edit.putInt(TOTAL_GAMES, current_total_games)
                edit.putInt(BEST_SCORE, current_best_score)
                edit.putInt(CORRECT_SONGS, current_correct_song)
                edit.putInt(RANKING, current_ranking)
                edit.commit()
            }
        }

        fun getUser(ctx: Context): UserProfile{
            val sharedPref = getSharedPreferences(ctx)

            var handle = ""
            var username = ""
            var image = ""
            var email = ""
            var totalGames = -1
            var bestScore = -1
            var correctSongs = -1
            var ranking = -1

            if(sharedPref != null) {
                handle = sharedPref.getString(HANDLE, "").toString()
                username = sharedPref.getString(USERNAME, "").toString()
                image = sharedPref.getString(IMAGE, "").toString()
                email = sharedPref.getString(EMAIL, "").toString()
                totalGames = sharedPref.getInt(TOTAL_GAMES, 0)
                bestScore = sharedPref.getInt(BEST_SCORE, 0)
                correctSongs = sharedPref.getInt(CORRECT_SONGS, 0)
                ranking = sharedPref.getInt(RANKING, 0)
            }
            return UserProfile(handle, username, image, email, totalGames, bestScore, correctSongs, ranking)
        }


    }
}
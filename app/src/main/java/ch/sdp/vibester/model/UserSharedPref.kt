package ch.sdp.vibester.model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User
import com.google.firebase.database.*
import java.util.*

/**
 * This static class store the users data offline + in the db
 */
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
        val ONLINE = "online"

        var dbAccess : DataGetter = DataGetter()


        private fun getSharedPreferences(ctx: Context): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        /**
         * Reset the current user stored locally with a retrieved one from db
         * @param Context The app context
         * @param email the email of the user
         * @param online if false, does not store in db
         */
        fun userReset(ctx: Context, email: String, online: Boolean = true){
            setUser(ctx, User(email=email), online)
            if (online){
                dbAccess = DataGetter()
                val call = {prof:User -> setUser(ctx, prof, online)}
                dbAccess.getUserData(call)
            }
        }

        /**
         * Set the user locally by hand
         * @param Context The app context
         * @param UserProfile the profile of the user
         * @param online if false, does not store in db
         */
        fun setUser(ctx: Context, user: User, online: Boolean){
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
                edit.putBoolean(ONLINE, online)
                edit.commit()
            }
        }

        /**
         * Update the score locally and in db (if enabled for the user)
         * @param Context The app context
         * @param deltaTotal how much increase the ranking
         * @param deltaBest how much increase best_score
         * @param deltaCorrect how much increase correct guesses song
         * @param deltaRanking How much increase ranking
         */
        fun updateScore(ctx: Context, deltaTotal: Int = 0, deltaBest: Int = 0, deltaCorrect: Int = 0, deltaRanking: Int = 0){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                val current_total_games = sharedPref.getInt(TOTAL_GAMES, 0) + deltaTotal
                val current_best_score = sharedPref.getInt(BEST_SCORE, 0) + deltaBest
                val current_correct_song = sharedPref.getInt(CORRECT_SONGS, 0) + deltaCorrect
                val current_ranking = sharedPref.getInt(RANKING, 0) + deltaRanking
                if(sharedPref.getBoolean(ONLINE, false)) {
                    dbAccess.updateFieldInt("testUser", current_total_games, "totalGames")
                    dbAccess.updateFieldInt("testUser", current_ranking, "ranking")
                    dbAccess.updateFieldInt("testUser", current_correct_song, "correctSongs")
                    dbAccess.updateFieldInt("testUser", current_best_score, "bestScore")
                }
                edit.putInt(TOTAL_GAMES, current_total_games)
                edit.putInt(BEST_SCORE, current_best_score)
                edit.putInt(CORRECT_SONGS, current_correct_song)
                edit.putInt(RANKING, current_ranking)
                edit.commit()
            }
        }

        /**
         * Update the username locally and in db (if enabled for the user)
         * @param Context The app context
         * @param username new username
         */
        fun updateUsername(ctx: Context, username: String){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                edit.putString(USERNAME, username)
                if(sharedPref.getBoolean(ONLINE, false)) {
                    dbAccess.updateFieldString("testUser", username, "username")
                }
                edit.commit()
            }
        }

        /**
         * Update the handle locally and in db (if enabled for the user)
         * @param Context The app context
         * @param handle new handle
         */
        fun updateHandle(ctx: Context, handle: String){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                edit.putString(HANDLE, handle)
                if(sharedPref.getBoolean(ONLINE, false)) {
                    dbAccess.updateFieldString("testUser", handle, "handle")
                }
                edit.commit()
            }
        }


        /**
         * Getter for locally store profile
         * @param Context The app context
         * @return UserProfile
         */
        fun getUser(ctx: Context): User {
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
            return User(handle, username, image, email, totalGames, bestScore, correctSongs, ranking)
        }


    }
}
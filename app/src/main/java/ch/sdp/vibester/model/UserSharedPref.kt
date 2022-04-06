package ch.sdp.vibester.model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.profile.UserProfile
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import java.util.*


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

        var dbAccess : UsersRepo = UsersRepo()

        /*
        val database: FirebaseDatabase = Firebase.database("https://vibester-sdp-default-rtdb.europe-west1.firebasedatabase.app")
        private lateinit var databaseRef: DatabaseReference
        //databaseRef = database.reference
                /*
                    https://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state#:~:text=When%20users%20log%20in%20to,direct%20to%20the%20login%20page.
                 */
 */
        fun getSharedPreferences(ctx: Context): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }


        fun userReset(ctx: Context, email: String){
            dbAccess = UsersRepo()
            setUser(ctx, UserProfile(email=email))
            val call = {prof:UserProfile -> setUser(ctx, prof)}
            dbAccess.getUserData(email, call)
        }

        fun setUser(ctx: Context, user: UserProfile){
            val edit = getSharedPreferences(ctx)?.edit()
            if (edit != null) {
                Log.e("HELLO", "My Friends Changes")
                edit.putString(HANDLE, user.handle)
                edit.putString(USERNAME, user.username)
                edit.putString(IMAGE, user.image)
                edit.putString(EMAIL, user.email)
                edit.putInt(TOTAL_GAMES, user.totalGames)
                edit.putInt(BEST_SCORE, user.bestScore)
                edit.putInt(CORRECT_SONGS, user.correctSongs)
                edit.putInt(RANKING, user.ranking)
                edit.commit()
                //databaseRef = database.reference

            }
        }


/*
        private fun queryDatabase(email: String) {
            var user: UserProfile

            val userRef = database.getReference("users")
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataSnapShot in dataSnapshot.children) {
                        val dbContents: Map<String, Objects> = dataSnapShot.value as Map<String, Objects>
                        if(dbContents["email"].toString() == email) {
                            user = UserProfile(
                                dbContents["handle"].toString(),
                                dbContents["username"].toString(),
                                dbContents["image"].toString(),
                                dbContents["email"].toString(),
                                dbContents["totalGames"].toString().toInt(),
                                dbContents["bestScore"].toString().toInt(),
                                dbContents["correctSongs"].toString().toInt()
                            )
                            //setupProfile(user)
                            break
                        }
                        else {
                            //setupProfile(UserProfile())
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAGGGG", "loadUsers:onCancelled", databaseError.toException())
                }
            })

        }

 */

        fun updateScore(ctx: Context, deltaTotal: Int = 0, deltaBest: Int = 0, deltaCorrect: Int = 0, deltaRanking: Int = 0){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                val current_total_games = sharedPref.getInt(TOTAL_GAMES, 0) + deltaTotal
                val current_best_score = sharedPref.getInt(BEST_SCORE, 0) + deltaBest
                val current_correct_song = sharedPref.getInt(CORRECT_SONGS, 0) + deltaCorrect
                val current_ranking = sharedPref.getInt(RANKING, 0) + deltaRanking
                dbAccess.updateField("testUser", current_total_games, "totalGames")
                dbAccess.updateField("testUser", current_ranking, "ranking")
                dbAccess.updateField("testUser", current_correct_song, "correctSongs")
                dbAccess.updateField("testUser", current_best_score, "bestScore")
                edit.putInt(TOTAL_GAMES, current_total_games)
                edit.putInt(BEST_SCORE, current_best_score)
                edit.putInt(CORRECT_SONGS, current_correct_song)
                edit.putInt(RANKING, current_ranking)
                edit.commit()
            }
        }


        fun updateUsername(ctx: Context, username: String){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                edit.putString(USERNAME, username)
                dbAccess.updateField("testUser", username, "username")
                edit.commit()
            }
        }

        fun updateHandle(ctx: Context, handle: String){
            val sharedPref = getSharedPreferences(ctx)
            if(sharedPref != null){
                val edit = sharedPref.edit()
                edit.putString(HANDLE, handle)
                dbAccess.updateField("testUser", handle, "handle")
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
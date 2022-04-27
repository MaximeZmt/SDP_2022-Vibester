package ch.sdp.vibester.database

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * The Database objects that returns our database instance
 */

object Database {
    fun get(): FirebaseDatabase {
        return  Firebase.database("https://vibester-sdp-default-rtdb.europe-west1.firebasedatabase.app")
    }
}
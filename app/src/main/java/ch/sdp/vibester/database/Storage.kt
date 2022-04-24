package ch.sdp.vibester.database

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

/**
 * The Database objects that returns our storage instance
 */

object Storage {
    fun get(): FirebaseStorage {
        return Firebase.storage("gs://vibester-sdp.appspot.com")
    }
}
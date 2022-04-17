package ch.sdp.vibester.database

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import ch.sdp.vibester.profile.UserProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

/**
 * The users class which handled all the interactions with the database that are linked to users
 */

class ImageRepo @Inject constructor() {
    private val storageRef = Storage.get().reference

    fun uploadFile(filePath: String, fileUri: Uri, callback: () -> Unit): Unit {
        val profilePicRef = storageRef.child(filePath)

        var uploadTask = profilePicRef.putFile(fileUri)

        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        uploadTask.addOnSuccessListener { taskSnapshot ->
            callback()
        }
    }
}
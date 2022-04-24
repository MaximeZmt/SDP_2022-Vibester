package ch.sdp.vibester.database

import android.net.Uri
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
package ch.sdp.vibester.database

import android.net.Uri
import com.google.android.gms.tasks.OnSuccessListener
import javax.inject.Inject

/**
 * The users class which handled all the interactions with the database that are linked to users
 */

class ImageGetter @Inject constructor() {
    private val storageRef = Storage.get().reference

    fun uploadFile(filePath: String, fileUri: Uri, callback: () -> Unit) {
        val profilePicRef = storageRef.child(filePath)

        var uploadTask = profilePicRef.putFile(fileUri)

        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        uploadTask.addOnSuccessListener {
            callback()
        }
    }

    fun fetchImage(imageID: String, callback: (downloadUR: Uri) -> Unit) {
        storageRef.child("profileImg/1jzr3IhJ1K").downloadUrl.addOnSuccessListener(
            OnSuccessListener(callback))
    }

}
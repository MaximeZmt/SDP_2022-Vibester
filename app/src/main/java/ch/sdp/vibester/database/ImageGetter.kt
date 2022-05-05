package ch.sdp.vibester.database

import android.net.Uri
import com.google.android.gms.tasks.OnSuccessListener
import javax.inject.Inject

/**
 * The users class which handled all the interactions with the database that are linked to users
 */

class ImageGetter @Inject constructor() {
    private val storageRef = Storage.get().reference

    /**
     * This function uploads a provided image to the database
     * @param filePath file path of the picture on the phone
     * @param fileUri URI of the image on the database
     * @param callback function to be called when image is done uploading
     */
    fun uploadFile(filePath: String, fileUri: Uri, callback: () -> Unit) {
        val profilePicRef = storageRef.child(filePath)

        val uploadTask = profilePicRef.putFile(fileUri)

        uploadTask.addOnSuccessListener { callback() }
    }

    /**
     * This function fetches an image
     * @param imageID ID of the image in the database
     * @param callback function to be called when image fetched
     */
    fun fetchImage(imageID: String, callback: (downloadUR: Uri) -> Unit) {
        storageRef.child(imageID).downloadUrl.addOnSuccessListener(
            OnSuccessListener(callback))
    }

}
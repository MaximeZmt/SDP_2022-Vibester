package ch.sdp.vibester.database

import android.content.ContentValues
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

class UsersRepo @Inject constructor() {
    private val dbRef = Database.get().getReference("users")


    /**
     * The users class which handled all the interactions with the database that are linked to users
     * @param userID the id of the user which is being updated
     * @param newVal the new value of the field that is being updated
     * @param fieldName the field name of the field that is being updated
     */
    fun updateField(userID: String, newVal: String, fieldName: String) {
        dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "-Myfy9TlCUTWYRxVLBsQ"
            .child(fieldName)
            .setValue(newVal)
    }

    /**
     * The users class which handled all the interactions with the database that are linked to users
     * @param email the of the user
     * @param callback the callback function to be called when the data of the appropriate user is available
     */
    fun getUserData(email: String, callback: (UserProfile) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapShot in dataSnapshot.children) {
                    val dbContents = dataSnapShot.getValue<UserProfile>()
                    if (dbContents != null) {
                        if(dbContents.email == email) {
                            callback(dbContents)
                            break
                        } else {
                            callback(UserProfile())
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadUsers:onCancelled", databaseError.toException())
            }
        })
    }
}
package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.profile.UserProfile
import ch.sdp.vibester.util.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

/**
 * The users class which handled all the interactions with the database that are linked to users
 */

class DataGetter @Inject constructor() {
    private val dbRef = Database.get().getReference("users")

    // Constructor
    init {
        Log.e("DATAGETTER","Hello world")
    }

    /**
     * This function updates a specific field of a user in the database
     * @param userID the id of the user which is being updated
     * @param newVal (String) the new value of the field that is being updated
     * @param fieldName the field name of the field that is being updated
     */
    fun updateFieldString(userID: String, newVal: String, fieldName: String) {
        dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "testUser"
            .child(fieldName)
            .setValue(newVal)
    }

    /**
     * The users class which handled all the interactions with the database that are linked to users
     * @param userID the id of the user which is being updated
     * @param newVal (Int) the new value of the field that is being updated
     * @param fieldName the field name of the field that is being updated
     */
    fun updateFieldInt(userID: String, newVal: Int, fieldName: String) {
        dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "testUser"
            .child(fieldName)
            .setValue(newVal)
    }

    fun updateRelativeFieldInt(userID: String, newVal: Int, fieldName: String) {
        dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "testUser"
            .child(fieldName)
            .get().addOnCompleteListener { t ->
                dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "testUser"
                    .child(fieldName)
                    .setValue((t.result.value as Int?)!! + newVal)
            }

    }


    /**
     * The users class which handled all the interactions with the database that are linked to users
     * This function creates a new user account in the database
     * @param email the email of the new user
     * @param username the username of the new user
     * @param handle the handle of the new user
     * @param callback function to be called when the the user has been created
     */
    fun createUser(email: String, username: String, handle: String, callback: (String) -> Unit) {
        var newUser = UserProfile(handle, username, "", email, 0, 0, 0, 0
        )

        val newId = Util.createNewId()

        dbRef.child(newId).setValue(newUser)
            .addOnSuccessListener {
                callback(email)
            }
    }

    /**
     * This functions fetches the data from the database
     * @param email the of the user
     * @param callback the function to be called when the data of the appropriate user is available
     */
    fun getUserData(callback: (UserProfile) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapShot in dataSnapshot.children) {
                    val dbContents = dataSnapShot.getValue<UserProfile>()
                    if (dbContents != null) {
                        if(FireBaseAuthenticator.isLoggedIn() && dbContents.email == FireBaseAuthenticator.getCurrentUserMail()) {
                            DbUserIdStore.storeUID(dataSnapShot.key!!)
                            callback(dbContents)
                            break
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
package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
import ch.sdp.vibester.user.User
import ch.sdp.vibester.util.Util
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
     * This function updates a specific string field of a user in the database
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
     * This function updates a specific int field of a user in the database
     * @param userID the id of the user which is being updated
     * @param newVal (Int) the new value of the field that is being updated
     * @param fieldName the field name of the field that is being updated
     */
    fun updateFieldInt(userID: String, newVal: Int, fieldName: String) {
        dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "testUser"
            .child(fieldName)
            .setValue(newVal)
    }


    /**
     * This function creates a new user account in the database
     * @param email the email of the new user
     * @param username the username of the new user
     * @param handle the handle of the new user
     * @param callback function to be called when the the user has been created
     */
    fun createUser(email: String, username: String, handle: String, callback: (String) -> Unit) {
        var newUser = User(handle, username, "", email, 0, 0, 0, 0
        )

        val newId = Util.createNewId()

        dbRef.child(newId).setValue(newUser)
            .addOnSuccessListener {
                callback(email)
            }
    }

    /**
     * This functions fetches the data of the given user from the database
     * @param email the of the user
     * @param callback the function to be called when the data of the appropriate user is available
     */
    fun getUserData(email: String, callback: (User) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapShot in dataSnapshot.children) {
                    val dbContents = dataSnapShot.getValue<User>()
                    if (dbContents != null) {
                        if(dbContents.email == email) {
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
package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
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

class UsersRepo @Inject constructor() {
    private val dbRef = Database.get().getReference("users")

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


    /**
     * Update a subfield of user's field
     * @param userID the id of the user which is being updated
     * @param newVal (Boolean) the new value of the field that is being updated
     * @param fieldName the field name of the field that is being updated
     * @param subFieldName the field name of the field that is being updated
     */
    fun updateFieldSubFieldBoolean(userID: String, newVal: Boolean, fieldName: String, subFieldName: String) {
        dbRef.child(userID)
            .child(fieldName)
            .child(subFieldName)
            .setValue(newVal)
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
        val newUser = UserProfile(handle, username, "", email, 0, 0, 0, 0)
        val newId = Util.createNewId()
        dbRef.child(newId).setValue(newUser)
            .addOnSuccessListener {
                callback(email)
            }
    }

    /**
     * Search for users by its any field in Firebase Realtime Database
     * @param field user fields used for a search
     * @param searchInput search text inputed by user
     * @param callback function to call with found users by username
     *
     * Comment about \uf8ff:
     * The \uf8ff character used in the query above is a very high code point in the Unicode range.
     * Because it is after most regular characters in Unicode, the query matches all values that start with a inputUsername.
     */
    fun searchByField(field: String, searchInput: String, callback:(ArrayList<UserProfile>) -> Unit) {
        val queryUsers = dbRef
            .orderByChild(field)
            .startAt(searchInput)
            .endAt(searchInput+"\uf8ff")

        val users: ArrayList<UserProfile> = ArrayList()
        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (snapshot in dataSnapshot.children) {
                    val userProfile:UserProfile? = snapshot.getValue(UserProfile::class.java)
                    if (userProfile != null) {
                        users.add(userProfile)
                    }
                }
                return callback(users)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "searchForUsers:onCancelled", error.toException())
            }
        })
    }

    /**
     * This functions fetches the data from the database
     * @param email the of the user
     * @param callback the function to be called when the data of the appropriate user is available
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
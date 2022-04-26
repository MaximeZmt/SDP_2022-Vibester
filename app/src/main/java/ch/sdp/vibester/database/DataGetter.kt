package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
import ch.sdp.vibester.auth.FireBaseAuthenticator
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

class DataGetter @Inject constructor() {
    private val dbRef = Database.get().getReference("users")
    val authenticator: FireBaseAuthenticator = FireBaseAuthenticator()


    // Constructor
    init {
        Log.e("DATAGETTER","Hello world")
    }

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
     * This function creates a new user account in the database
     * @param email the email of the new user
     * @param username the username of the new user
     * @param callback function to be called when the the user has been created
     */
    fun createUser(email: String, username: String, callback: (String) -> Unit) {
        var newUser = User(username, "", email, 0, 0, 0, 0)
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
     */
    fun searchByField(field: String, searchInput: String, callback:(ArrayList<User>) -> Unit) {
        val queryUsers = dbRef
            .orderByChild(field)
            .startAt(searchInput)
            .endAt(searchInput+"\uf8ff")
        /**
         * Comment about \uf8ff:
         * The \uf8ff character used in the query above is a very high code point in the Unicode range.
         * Because it is after most regular characters in Unicode, the query matches all values that start with a inputUsername.
         */

        val users: ArrayList<User> = ArrayList()
        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (snapshot in dataSnapshot.children) {
                    val userProfile:User? = snapshot.getValue(User::class.java)
                    if (userProfile != null) {
                        userProfile.uid = snapshot.key.toString()
                        users.add(userProfile)
                    }
                }
                return callback(users)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "searchByField:onCancelled", error.toException())
            }
        })
    }

    /**
     * This functions fetches the data of the given user from the database
     * @param email the of the user
     * @param callback the function to be called when the data of the appropriate user is available
     */
    fun getUserData(callback: (User) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapShot in dataSnapshot.children) {
                    val dbContents = dataSnapShot.getValue<User>()
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
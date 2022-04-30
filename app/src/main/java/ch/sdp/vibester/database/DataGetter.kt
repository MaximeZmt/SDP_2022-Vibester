package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

/**
 * The users class which handled all the interactions with the database that are linked to users
 */

class DataGetter @Inject constructor() {
    private val dbUserRef = Database.get().getReference("users")
    private val dbRoomRef = Database.get().getReference("rooms")
    private val authenticator: FireBaseAuthenticator = FireBaseAuthenticator()


    /**
     * This function updates a specific string field of a user in the database
     * @param userID the id of the user which is being updated
     * @param newVal integer value to update
     * @param fieldName field name to updated
     */
    fun updateFieldString(userID: String, newVal: String, fieldName: String) {
        dbUserRef.child(userID)
            .child(fieldName)
            .setValue(newVal)
    }

    /**
     * This function updates a specific int field of a user in the database
     * @param userID
     * @param newVal integer value to update
     * @param fieldName field name to updated
     */
    fun updateFieldInt(userID: String, newVal: Int, fieldName: String) {
        dbUserRef.child(userID)
            .child(fieldName)
            .setValue(newVal)
    }


    /**
     * This function increments a specific int field of a user in the database
     * @param userID
     * @param newVal integer value to update
     * @param fieldName field name to updated
     */
    fun updateRelativeFieldInt(userID: String, newVal: Int, fieldName: String) {
        dbUserRef.child(userID).child(fieldName)
            .get().addOnSuccessListener { t ->
                var sum  = newVal
                if(t.value != null){sum += (t.value as Long?)!!.toInt()}
                updateFieldInt(userID, sum, fieldName)
            }

    }

    /**
     * This function sets the best between current and given in argument value
     * @param userID the id of the user which is being updated
     * @param newVal integer value to update
     * @param fieldName field name to updated
     */
    fun updateBestFieldInt(userID: String, newVal: Int, fieldName: String) {
        dbUserRef.child(userID).child(fieldName)
            .get().addOnSuccessListener { t ->
                var maxValue = newVal
                if(t.value != null){maxValue = maxOf((t.value as Long?)!!.toInt(), newVal)}
                updateFieldInt(userID, maxValue, fieldName)
                }
    }

    /**
     * Update the best value of subfield
     * @param userID
     * @param newVal integer value to update
     * @param fieldName field name to updated
     * @param subFieldName subfield name to updated
     */
    fun updateBestSubFieldInt(userID: String, newVal: Int, fieldName: String, subFieldName: String) {
        dbUserRef.child(userID).child(fieldName).child(subFieldName)
            .get().addOnSuccessListener { t ->
                var maxValue = newVal
                if(t.value != null){maxValue = maxOf((t.value as Long?)!!.toInt(), newVal)}
                updateFieldSubFieldInt(userID, maxValue, fieldName, subFieldName)
            }
    }


    /**
     * Update Boolean subfield of user's field
     * @param userID
     * @param newVal integer value to update
     * @param fieldName field name to updated
     * @param subFieldName subfield name to updated
     */
    fun updateFieldSubFieldBoolean(userID: String, newVal: Boolean, fieldName: String, subFieldName: String) {
        dbUserRef.child(userID)
            .child(fieldName)
            .child(subFieldName)
            .setValue(newVal)
    }

    /**
     * Update Integer subfield of user's field
     * @param userID
     * @param newVal integer value to update
     * @param fieldName field name to updated
     * @param subFieldName subfield name to updated
     */
    fun updateFieldSubFieldInt(userID: String, newVal: Int, fieldName: String, subFieldName: String) {
        dbUserRef.child(userID)
            .child(fieldName)
            .child(subFieldName)
            .setValue(newVal)
    }

    /**
     * This function creates a new user account in the database
     * @param email the email of the new user
     * @param username the username of the new user
     * @param callback function to be called when the the user has been created
     * @param uid id of the new user
     */
    fun createUser(email: String, username: String, callback: (String) -> Unit, uid: String) {
        var newUser = User(username, "", email, 0, 0, 0, 0, uid)
        dbUserRef.child(uid).setValue(newUser)
            .addOnSuccessListener {
                callback(email)
            }
    }

    /**
     * This function creates a new room in the database
     * @param roomName the name of the new room
     * @param callback function to be called when the room has been created
     */
    fun createRoom(roomName: String,  callback: (PartyRoom) -> Unit) {
        val partyRoom = PartyRoom()
        partyRoom.setRoomName(roomName)
        partyRoom.setEmailList(mutableListOf(authenticator.getCurrUser()?.email!!))
        val ref = dbRoomRef.push()
        val key = ref.key
        if (key != null) {
            partyRoom.setRoomID(key)
        }
        ref.setValue(partyRoom)
        callback(partyRoom)
    }


    /**
     * Search for users by its any field in Firebase Realtime Database
     * @param field user fields used for a search
     * @param searchInput search text inputed by user
     * @param callback function to call with found users by username
     */
    fun searchByField(field: String, searchInput: String, callback:(ArrayList<User>) -> Unit) {
        val queryUsers = dbUserRef
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
     * @param callback the function to be called when the data of the appropriate user is available
     */
    fun getUserData(callback: (User) -> Unit) {
        dbUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapShot in dataSnapshot.children) {
                    val dbContents = dataSnapShot.getValue<User>()
                    if (dbContents != null) {
                        if(FireBaseAuthenticator.isLoggedIn() && dbContents.email == FireBaseAuthenticator.getCurrentUserMail()) {
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

    /**
     * This functions that updates the users of a room
     * @param partyRoom the new room
     */
    fun updateRoomUserList(partyRoom: PartyRoom) {
        dbRoomRef.child(partyRoom.getRoomID()).child("emailList").setValue(partyRoom.getEmailList())
    }

    /**
     * This functions fetches the data of the given user from the database
     * @param roomName the name of the room to retrieve data from
     * @param callback the function to be called when the data of the appropriate user is available
     */

    fun getRoomData(roomName: String, callback: (PartyRoom) -> Unit) {
        val queryRooms = dbRoomRef
            .orderByChild("roomName")
            .equalTo(roomName)

        queryRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val partyRoom: PartyRoom? = snapshot.getValue(PartyRoom::class.java)
                    if(partyRoom != null) {
                        val currUserEmail = authenticator.getCurrUser()?.email!!
                        if(!partyRoom.getEmailList().contains(currUserEmail)) {
                            partyRoom.addUserEmail(currUserEmail)
                            updateRoomUserList(partyRoom)
                        }
                        callback(partyRoom)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "getRoomData:onCancelled", error.toException())
            }
        })
    }
}
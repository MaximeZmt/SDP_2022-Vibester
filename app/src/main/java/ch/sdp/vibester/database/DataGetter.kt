package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.helper.ArtistIdentifier
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
/**
 * The users class which handled all the interactions with the database that are linked to users
 */

class DataGetter @Inject constructor() {
    private val dbUserRef = Database.get().getReference("users")
    private val dbRoomRef = Database.get().getReference("rooms")
    private val authenticator: FireBaseAuthenticator = FireBaseAuthenticator()

    /**
     * Set field value
     * @param uid user identifier
     * @param newVal new value to set
     * @param fieldName
     */
    fun setFieldValue(uid: String, fieldName: String, newVal: Any) {
        dbUserRef.child(uid).child(fieldName).setValue(newVal)
    }

    /**
     * Set subfield value
     * @param uid user identifier
     * @param newVal new value to set
     * @param fieldName
     * @param subFieldName
     */
    fun setSubFieldValue(uid: String, fieldName: String, subFieldName: String, newVal: Any) {
        dbUserRef.child(uid).child(fieldName).child(subFieldName).setValue(newVal)
    }


    /**
     * Update integer value in a subfield based on method sum/best
     * @param uid User ID
     * @param newVal
     * @param fieldName
     */
    fun updateFieldInt(uid: String, fieldName: String, newVal: Int, method:String) {
        dbUserRef.child(uid).child(fieldName)
            .get().addOnSuccessListener { t ->
                var finalVal  = checkValue(t, method, newVal)//newVal TO DELETE IF TESTS PASS, OTHERWISE ROLL BACK!
                setFieldValue(uid, fieldName, finalVal)
            }
    }


    /**
     * Update the integer value of subfield based on method sum/best
     * @param userID
     * @param newVal
     * @param fieldName
     * @param subFieldName
     */
    fun updateSubFieldInt(userID: String, newVal: Int, fieldName: String, subFieldName: String, method: String) {
        dbUserRef.child(userID).child(fieldName).child(subFieldName)
            .get().addOnSuccessListener { t ->
                var finalVal = checkValue(t, method, newVal) //newVal: TO DELETE IF TESTS PASS, OTHERWISE ROLL BACK!
                setSubFieldValue(userID, fieldName, subFieldName, finalVal)
            }
    }

    /**
     * Depending on the value of the snapshot and the method with which we change the value,
     * returns either the sum or the best value for the finalVal.
     * @param t         : DataSnapshot
     * @param method    : String, mode with which we assign finalVal
     * @param newVal    : Int, value to compare to previousVal
     */
    private fun checkValue(t: DataSnapshot, method: String, newVal: Int): Int {
        var finalVal = newVal
        if(t.value != null) {
            val previousVal = (t.value as Long?)!!.toInt()
            when (method) {
                "sum" -> finalVal += previousVal
                "best" -> finalVal = maxOf(previousVal, newVal)
            }
        }
        return finalVal
    }


    /**
     * This function creates a new user account in the database
     * @param email the email of the new user
     * @param username the username of the new user
     * @param callback function to be called when the the user has been created
     * @param uid id of the new user
     */
    fun createUser(email: String, username: String, callback: (String) -> Unit, uid: String) {
        val newUser = User(email = email, username = username, uid = uid)
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
    fun createRoom(roomName: String,  callback: (PartyRoom, String) -> Unit) {
        val partyRoom = PartyRoom()
        partyRoom.setRoomName(roomName)
        partyRoom.setEmailList(mutableListOf(authenticator.getCurrUser()?.email!!))
        val ref = dbRoomRef.push()
        val key = ref.key
        if (key != null) {
            partyRoom.setRoomID(key)
        }
        ref.setValue(partyRoom)
        if (key != null) {
            callback(partyRoom, key)
        }
    }


    /**
     * Search for users by its any field in Firebase Realtime Database
     * @param field user fields used for a search
     * @param searchInput search text inputted by user
     * @param callback function to call with found users by username
     * @param callbackUid function to call with found uid
     */
    fun searchByField(field: String, searchInput: String, callback:(ArrayList<User>) -> Unit, callbackUid :(ArrayList<String>) -> Unit) {
        val uidList: ArrayList<String> = ArrayList()

        val queryUsers = //Not very pretty, but otherwise CodeClimate complains about 26 lines
            dbUserRef.orderByChild(field)
            .startAt(searchInput).endAt(searchInput+"\uf8ff")

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
                        uidList.add(userProfile.uid)
                        users.add(userProfile)
                    }
                }
                callback(users)
                callbackUid(uidList)
                queryUsers.removeEventListener(this);
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "searchByField:onCancelled", error.toException())
            }
        })
    }

    /**
     * This functions fetches the data of the given user from the database
     * @param userId
     * @param callback the function to be called when the data of the appropriate user is available
     */
    fun getUserData(userId: String, callback: (User) -> Unit) {
        dbUserRef.child(userId).get().addOnSuccessListener {
            it.getValue<User>()?.let { it1 -> callback(it1) }
        }.addOnFailureListener{
            Log.d("DataGetter", "getUserData:onCancelled", it)
        }
    }

    /**
     * This functions that updates the users of a room
     * @param partyRoom the new room
     */
    fun updateRoomUserList(partyRoom: PartyRoom) {
        dbRoomRef.child(partyRoom.getRoomID()).child("emailList").setValue(partyRoom.getEmailList())
    }

    fun getCurrentUser(): FirebaseUser? {
        return authenticator.getCurrUser()
    }

    /**
     * This functions fetches the data of the given user from the database
     * @param roomName the name of the room to retrieve data from
     * @param partyRoomCallback the function to be called when the data of the appropriate room data is available
     * @param songListCallback the function to be called when the data of the appropriate song list is available

     */

    fun getRoomData(roomName: String,
                    partyRoomCallback: (PartyRoom, String) -> Unit,
                    songListCallback: (MutableList<Pair<String, String>>) -> Unit) {

        val queryRooms = dbRoomRef
            .orderByChild("roomName")
            .equalTo(roomName)

        queryRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val partyRoom: PartyRoom? = snapshot.getValue(PartyRoom::class.java)
                    if(partyRoom != null) {
                        val currUserEmail = getCurrentUser()?.email!!
                        if(!partyRoom.getEmailList().contains(currUserEmail)) {
                            partyRoom.addUserEmail(currUserEmail)
                            updateRoomUserList(partyRoom)
                        }
                        partyRoomCallback(partyRoom, partyRoom.getRoomID())
                    }
                        val snapshotMap = snapshot.value as Map<String, Object>
                        val songList = snapshotMap["songList"] as List<*>
                        var gameSongList: MutableList<Pair<String, String>> = mutableListOf()
                        for (song in songList) {
                            val tempPair: Map<String, String> = song as Map<String, String>
                            gameSongList.add(
                                Pair(
                                    tempPair.getOrDefault("first", ""),
                                    tempPair.getOrDefault("second", "")
                                )
                            )
                        }
                        songListCallback(gameSongList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "getRoomData:onCancelled", error.toException())
            }
        })
    }

    /**
     * This functions that updates the song list of the room
     * @param roomID ID of the room
     * @param songList the new song list
     */

    fun updateSongList(roomID: String, songList: MutableList<Pair<String, String>>) {
        dbRoomRef.child("${roomID}/songList").setValue(songList)
    }

    /**
     * This functions that updates the the start game field
     * @param roomID ID of the room
     * @param value the new value to store
     */

    fun updateStartGame(roomID: String, value: Boolean) {
        dbRoomRef.child("${roomID}/gameStarted").setValue(value)
    }

    /**
     * This functions reads the start of the game field and calls the appropriate functions
     * @param roomName name of the room
     * @param callback callback to be called when the read value is available
     */

    fun readStartGame(roomName: String, callback: (Boolean) -> Unit) {
        val queryRooms = dbRoomRef
            .orderByChild("roomName")
            .equalTo(roomName)

        queryRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val snapshotMap = snapshot.getValue() as Map<String, Object>
                    val gameStarted: Boolean = snapshotMap["gameStarted"] as Boolean
                    callback(gameStarted)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "getRoomData:onCancelled", error.toException())
            }
        })

}
}




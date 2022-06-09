package ch.sdp.vibester.database

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.reflect.KFunction0

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
     * Set user with userId to follow user with followingId
     * @param userId id of of the current user
     * @param followingId id of the user to be following
     */
    fun setFollowing(userId: String, followingId: String) {
        setSubFieldValue(userId, "following", followingId, true)
    }

    /**
     * Set user with userId to unfollow user with followingId
     * @param userId if of the current user
     * @param followingId id of the user to be unfollowing
     */
    fun setUnfollow(userId: String, followingId: String) {
        setSubFieldValue(userId, "following", followingId, false)
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
                val finalVal  = checkValue(t, method, newVal)//newVal TO DELETE IF TESTS PASS, OTHERWISE ROLL BACK!
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
                val finalVal = checkValue(t, method, newVal) //newVal: TO DELETE IF TESTS PASS, OTHERWISE ROLL BACK!
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
        if (t.value != null) {
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
     * @param uid id of the new user
     */
    fun createUser(email: String, username: String, uid: String) {
        val newUser = User(email = email, username = username, uid = uid)
        dbUserRef.child(uid).setValue(newUser)
    }

    /**
     * This function creates a new room in the database
     * @param callback function to be called when the room has been created
     */
    fun createRoom(callback: (PartyRoom, String) -> Unit) {
        val partyRoom = PartyRoom()
        partyRoom.setEmailList(mutableListOf(authenticator.getCurrUser()?.email!!))

        val charPool : List<Char> = ('0'..'9') + ('a'..'z')

        val randomString = (1..6)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        partyRoom.setRoomID(randomString)

        dbRoomRef.child(randomString).setValue(partyRoom)

        callback(partyRoom, randomString)

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
                queryUsers.removeEventListener(this)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "searchByField:onCancelled", error.toException())
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
            val msg = "getUserData:onCancelled"
            Log.d("DataGetter", msg, it)
        }
    }

    /**
     * This functions that updates the users of a room
     * @param partyRoom the new room
     */
    fun updateRoomUserList(partyRoom: PartyRoom) {
        dbRoomRef.child(partyRoom.getRoomID()).child("emailList").setValue(partyRoom.getEmailList())
    }

    fun updateRoomScore(score: Pair<String, Int>, roomID: String) {
        dbRoomRef.child(roomID).child("scores").push().setValue(score)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authenticator.getCurrUser()
    }
//    callback: ((HashMap<String, Int>) -> Unit)
    fun readScores(roomID: String, callback: (HashMap<String, Int>) -> Unit) {
        val queryRooms = dbRoomRef.child(roomID).child("scores")

        val scoresMap = hashMapOf<String, Int>()

        queryRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (score in dataSnapshot.children) {
                    val email = (score.value as HashMap<String, Object>)["first"] as String
                    val gameScore = ((score.value as HashMap<String, Object>)["second"] as Long).toInt()

                    scoresMap[email] = gameScore
                }
                callback(scoresMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }


    /**
     * This functions fetches the data of the given user from the database
     * @param roomID the name of the room to retrieve data from
     * @param partyRoomCallback the function to be called when the data of the appropriate room data is available
     * @param songListCallback the function to be called when the data of the appropriate song list is available
     */
    fun getRoomData(roomID: String,
                    partyRoomCallback: (PartyRoom, String) -> Unit,
                    songListCallback: (MutableList<Pair<String, String>>, Int, String, Int) -> Unit) {

        dbRoomRef.orderByChild("roomID").equalTo(roomID)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val partyRoom: PartyRoom? = snapshot.getValue(PartyRoom::class.java)
                        if (partyRoom != null) {
                            val currUserEmail = getCurrentUser()?.email!!
                            if (!partyRoom.getEmailList().contains(currUserEmail)) {
                                partyRoom.addUserEmail(currUserEmail)
                                updateRoomUserList(partyRoom)
                            }
                            partyRoomCallback(partyRoom, partyRoom.getRoomID())
                        }

                        val gameSongList: MutableList<Pair<String, String>> = mutableListOf()

                        for (song in (snapshot.value as Map<String, Object>) ["songList"] as List<*>) {
                            val tempPair: Map<String, String> = song as Map<String, String>
                            gameSongList.add(
                                Pair(
                                    tempPair.getOrDefault("first", ""),
                                    tempPair.getOrDefault("second", "")
                                )
                            )
                        }
                        val gameSize: Int = ((snapshot.value as Map<String, Object>)["gameSize"] as Long).toInt()
                        val gameMode: String = (snapshot.value as Map<String, Object>)["gameMode"] as String
                        val difficultyLevel: Int = ((snapshot.value as Map<String, Object>)["difficulty"] as Long).toInt()
                        songListCallback(gameSongList, gameSize, gameMode, difficultyLevel)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "getRoomData:onCancelled", error.toException())
                }
        })
    }




    /**
     * This functions that updates the field of a room entry
     * @param roomID ID of the room
     * @param fieldName name of the field to update
     * @param value new value to write to the database
     */
    fun <T> updateRoomField(roomID: String, fieldName: String, value: T) {
        dbRoomRef.child("${roomID}/${fieldName}").setValue(value)
    }

    /**
     * This functions reads the start of the game field and calls the appropriate functions
     * @param roomID ID of the room
     * @param callback callback to be called when the read value is available
     */
    fun readStartGame(roomID: String, callback: (Boolean) -> Unit) {
        Log.w("DEBUG", roomID)
        val queryRooms = dbRoomRef.child(roomID).child("gameStarted")

        val startGameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<Boolean>()
                if (value != null) {
                  callback(value)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                val msg = "loadPost:onCancelled"
                val exception = databaseError.toException()
                Log.w(TAG, msg, exception)
            }
        }
        queryRooms.addValueEventListener(startGameListener)
    }
}
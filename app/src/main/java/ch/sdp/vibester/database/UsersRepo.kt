package ch.sdp.vibester.database

import android.content.ContentValues
import android.util.Log
import ch.sdp.vibester.profile.UserProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject


class UsersRepo @Inject constructor() {
    private val dbRef = Database.get().getReference("users")

    fun updateName(userID: String, newVal: String, type: String) {
        dbRef.child(userID) //For now ID is hardcoded, will generate it creating new users next week "-Myfy9TlCUTWYRxVLBsQ"
            .child(type)
            .setValue(newVal)
    }

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
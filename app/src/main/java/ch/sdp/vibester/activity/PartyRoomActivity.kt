package ch.sdp.vibester.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PartyRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_room)

//        val emailTxt = findViewById<TextView>(R.id.emails)

//        val getIntent = intent.extras

        val roomName = intent.getStringExtra("roomName").toString()
        fetchData(roomName)
        val createPartyRoom = intent.getBooleanExtra("createRoom", false)
    }

    fun setEmails(emailList: MutableList<String>) {
        findViewById<TextView>(R.id.emails).text = emailList.toString()
    }


    private fun fetchData(roomName: String) {
        val dbRef = Database.get().getReference("rooms")

        val queryUsers = dbRef
            .orderByChild("roomName")
            .startAt(roomName)
            .endAt(roomName+"\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val partyRoom: PartyRoom? = snapshot.getValue(PartyRoom::class.java)
                    if(partyRoom != null) {
                        setEmails(partyRoom.getEmailList())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "searchByField:onCancelled", error.toException())
            }
        })
    }
}
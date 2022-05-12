package ch.sdp.vibester.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PartyRoomActivity : AppCompatActivity() {
    @Inject
    lateinit var dataGetter: DataGetter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_room)

        val roomName = intent.getStringExtra("roomName").toString()
        val createPartyRoom = intent.getBooleanExtra("createRoom", false)
        if(createPartyRoom) {
            createRoom(roomName)
        }
        else {
            fetchData(roomName)
        }
    }

    private fun updateUI(partyRoom: PartyRoom) {
        findViewById<TextView>(R.id.emails).text = partyRoom.getEmailList().toString()
    }

    private fun fetchData(roomName: String) {
        dataGetter.getRoomData(roomName, this::updateUI)
    }

    private fun createRoom(roomName: String) {
        dataGetter.createRoom(roomName, this::updateUI)
    }

    private fun fetchCurrentSong() {

    }

    private fun playCurrentSong() {

    }
}
package ch.sdp.vibester.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import ch.sdp.vibester.R

class ChoosePartyRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_party_room)

        val createPartyRoom = findViewById<Button>(R.id.createParty)
        val joinPartyRoom = findViewById<Button>(R.id.joinParty)

        val partyRoomInput = findViewById<EditText>(R.id.roomNameInput)

        joinPartyRoom.setOnClickListener {
            switchToRoom(partyRoomInput.text.toString(), false)
        }

        createPartyRoom.setOnClickListener {
            switchToRoom(partyRoomInput.text.toString(), true)
        }
    }

    private fun switchToRoom(roomName: String, createRoom: Boolean) {
        val newIntent = Intent(this, PartyRoomActivity::class.java)
        newIntent.putExtra("roomID", roomName)
        newIntent.putExtra("createRoom", createRoom)

        startActivity(newIntent)
    }
}
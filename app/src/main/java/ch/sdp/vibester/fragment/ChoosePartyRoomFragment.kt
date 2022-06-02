package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.PartyRoomActivity
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.ViewModel

class ChoosePartyRoomFragment : Fragment(R.layout.activity_choose_party_room) {
    private var vmChooseRoom = ViewModel()
    private lateinit var gameManager : GameManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmChooseRoom.view = view
        vmChooseRoom.ctx = view.context
        val bundle = this.arguments
        if (bundle != null) {
            gameManager = bundle.get("gameManager") as GameManager
        }

        val createPartyRoom = vmChooseRoom.view.findViewById<Button>(R.id.createParty)
        val joinPartyRoom = vmChooseRoom.view.findViewById<Button>(R.id.joinParty)

        val partyRoomInput = vmChooseRoom.view.findViewById<EditText>(R.id.roomNameInput)


        joinPartyRoom.setOnClickListener {
            switchToRoom(partyRoomInput.text.toString(), true)
        }

        createPartyRoom.setOnClickListener {
            val bundle = bundleOf("gameManager" to gameManager)
            findNavController().navigate(R.id.fragment_genre_setup, bundle)
        }
    }

    private fun switchToRoom(roomName: String, joinRoom: Boolean) {
        val newIntent = Intent(vmChooseRoom.ctx, PartyRoomActivity::class.java)
        newIntent.putExtra("roomID", roomName)
        newIntent.putExtra("joinRoom", joinRoom)
        startActivity(newIntent)
    }
}
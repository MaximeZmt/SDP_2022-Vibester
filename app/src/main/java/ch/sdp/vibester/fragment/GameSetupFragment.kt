package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.BuzzerSetupActivity
import ch.sdp.vibester.activity.ChoosePartyRoomActivity
import ch.sdp.vibester.activity.LyricsBelongGameActivity
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.api.InternetState
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
* Game Setup fragment with a button in the bottom navigation.
*/
@AndroidEntryPoint
class GameSetupFragment : Fragment(R.layout.fragment_layout_game_setup){

    // TODO: OFFLINE
    private var hasInternet: Boolean = true
    private var vmGameSetup = ViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmGameSetup.view = view
        vmGameSetup.ctx = view.context

        setGameModeListeners()
        vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet).setOnClickListener { updateInternet(vmGameSetup.view.findViewById(R.id.game_setup_has_internet)) }
        updateInternet(vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet))
    }

    override fun onResume() {
        super.onResume()
        updateInternet(vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet))
    }

    private fun setGameModeListeners() {
        vmGameSetup.view.findViewById<Button>(R.id.local_buzzer_game_button)
            .setOnClickListener { chooseGame("local_buzzer", GameManager()) }
        vmGameSetup.view.findViewById<Button>(R.id.local_typing_game_button)
            .setOnClickListener { chooseGame("local_typing", GameManager()) }
        vmGameSetup.view.findViewById<Button>(R.id.local_lyrics_game_button)
            .setOnClickListener{ chooseGame("local_lyrics", GameManager()) }
        vmGameSetup.view.findViewById<Button>(R.id.online_buzzer_game_button)
            .setOnClickListener { chooseGame("online_buzzer", GameManager()) }
        vmGameSetup.view.findViewById<Button>(R.id.offline_game_button)
            .setOnClickListener { chooseGame("local_buzzer", GameManager(), true) }
    }

    /**
     * Set game mode. Set appropriate GameManager.
     */
    private fun chooseGame(gameMode: String, gameManager: GameManager, playOffline: Boolean = false){
        AppPreferences.setStr(getString(R.string.preferences_game_mode), gameMode)
        AppPreferences.setObject(gameManager,"gameManager")

        if (playOffline) {
            findNavController().navigate(R.id.fragment_setting_setup)
        }
        else if(gameMode == "online_buzzer"){
            findNavController().navigate(R.id.fragment_setting_setup)
        } else {
            findNavController().navigate(R.id.fragment_genre_setup)
        }
    }

    /**
     * Switches between Internet On and Internet Off when the view button is pressed.
     * @param view: The button responsible for internet toggling.
     */
    private fun updateInternet(view: View) {
        val btn: Button = view as Button
        val isConncted = InternetState.getInternetStatus(vmGameSetup.ctx)
        if (isConncted) {
            hasInternet = true
            btn.text = getString(R.string.GameSetup_internetSwitchOn)
            btn.setBackgroundColor(vmGameSetup.ctx.getColor(R.color.maximum_yellow_red))

            vmGameSetup.view.findViewById<LinearLayout>(R.id.horilayer_multi).visibility = VISIBLE
            vmGameSetup.view.findViewById<LinearLayout>(R.id.horilayer_single).visibility = VISIBLE
            vmGameSetup.view.findViewById<TextView>(R.id.singleplayer_game_txt).visibility = VISIBLE
            vmGameSetup.view.findViewById<TextView>(R.id.multiplayer_game_txt).visibility = VISIBLE

        } else {
            hasInternet = false
            btn.text = getString(R.string.GameSetup_internetSwitchOff)
            btn.setBackgroundColor(vmGameSetup.ctx.getColor(R.color.light_coral))

            vmGameSetup.view.findViewById<LinearLayout>(R.id.horilayer_multi).visibility = GONE
            vmGameSetup.view.findViewById<LinearLayout>(R.id.horilayer_single).visibility = GONE
            vmGameSetup.view.findViewById<TextView>(R.id.singleplayer_game_txt).visibility = GONE
            vmGameSetup.view.findViewById<TextView>(R.id.multiplayer_game_txt).visibility = GONE
        }
    }
}
package ch.sdp.vibester.fragment

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ch.sdp.vibester.R
import ch.sdp.vibester.api.InternetState
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.ViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        val bundle = bundleOf("gameManager" to gameManager)
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.main_bottom_nav_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (playOffline) {
            navController.navigate(R.id.fragment_setting_setup, bundle)
        }
        else if (gameMode == "online_buzzer"){
            navController.navigate(R.id.fragment_choose_online_room, bundle)
        }
        else {
            navController.navigate(R.id.fragment_genre_setup, bundle)
        }
    }

    /**
     * Switches between Internet On and Internet Off when the view button is pressed.
     * @param view: The button responsible for internet toggling.
     */
    private fun updateInternet(view: View) {
        val btn: Button = view as Button
        val isConnected = InternetState.getInternetStatus(vmGameSetup.ctx)
        if (isConnected) {
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
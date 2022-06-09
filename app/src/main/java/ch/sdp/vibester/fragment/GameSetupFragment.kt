package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.os.bundleOf
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.download.DownloadActivity
import ch.sdp.vibester.api.InternetState
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/**
* Game Setup fragment with a button in the bottom navigation.
*/
@AndroidEntryPoint
class GameSetupFragment : Fragment(R.layout.fragment_layout_game_setup){

    // TODO: OFFLINE
    private var hasInternet: Boolean = true
    private var vmGameSetup = ViewModel()
    private var test: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmGameSetup.view = view
        vmGameSetup.ctx = view.context

        val bundle = this.arguments
        if (bundle != null) {
            test = bundle.getBoolean("test", false)
        }

        setGameModeListeners()

        vmGameSetup.view.findViewById<AppCompatButton>(R.id.download).setOnClickListener {
            val newIntent = Intent(activity, DownloadActivity::class.java)
            startActivity(newIntent)
        }

        vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet).setOnClickListener { updateInternet(vmGameSetup.view.findViewById(R.id.game_setup_has_internet)) }
        updateInternet(vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet))
    }

    override fun onResume() {
        super.onResume()
        updateInternet(vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet))
    }


    private fun setGenreListeners(){
        val offline = vmGameSetup.view.findViewById<Button>(R.id.offline_game_button)
        val kpop = vmGameSetup.view.findViewById<Button>(R.id.kpopButton)
        val rock = vmGameSetup.view.findViewById<Button>(R.id.rockButton)
        val bts = vmGameSetup.view.findViewById<Button>(R.id.btsButton)
        val topTracks = vmGameSetup.view.findViewById<Button>(R.id.topTracksButton)
        val imagDragons = vmGameSetup.view.findViewById<Button>(R.id.imagDragonsButton)
        val billieEilish = vmGameSetup.view.findViewById<Button>(R.id.billieEilishButton)
        val validateSearch = vmGameSetup.view.findViewById<Button>(R.id.validateSearch)

        val records = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")

        if (!records.exists() || records.length() == 0L) {
            offline.setOnClickListener { Toast.makeText(it.context, "You don't have any downloaded song", Toast.LENGTH_SHORT).show() }
        }else{
            offline.setOnClickListener { chooseGame("local_buzzer", GameManager(), true) }
        }
        kpop.setOnClickListener { chooseGenreByTag("kpop", R.string.kpop) }
        rock.setOnClickListener { chooseGenreByTag("rock", R.string.rock) }
        bts.setOnClickListener { chooseGenreByArtist("BTS", R.string.gameGenre_bts) }
        topTracks.setOnClickListener { chooseGenre(method = LastfmMethod.BY_CHART.method, mode = R.string.top_tracks) }
        imagDragons.setOnClickListener{ chooseGenreByArtist("Imagine Dragons", R.string.gameGenre_imagine_dragons) }
        billieEilish.setOnClickListener { chooseGenreByArtist("Billie Eilish", R.string.gameGenre_billie_eilish) }
        validateSearch.setOnClickListener{ chooseGenreByArtist(searchArtistEditable.toString(), R.string.gameGenre_byArtistSearch) }
    }

    private fun chooseGenreByTag(tag: String, mode: Int) {
        chooseGenre(method = LastfmMethod.BY_TAG.method, tag = tag, mode = mode)
    }

    private fun chooseGenreByArtist(artist: String, mode: Int) {
        chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = artist, mode = mode)
    }


    private fun setGameModeListeners() {
        val localBuzzer = vmGameSetup.view.findViewById<Button>(R.id.local_buzzer_game_button)
        val localTyping = vmGameSetup.view.findViewById<Button>(R.id.local_typing_game_button)
        val localLyric = vmGameSetup.view.findViewById<Button>(R.id.local_lyrics_game_button)
        val onlineBuzzer = vmGameSetup.view.findViewById<Button>(R.id.online_buzzer_game_button)
        val offlineBuzzer = vmGameSetup.view.findViewById<Button>(R.id.offline_game_button)
        localBuzzer.setOnClickListener { chooseGame("local_buzzer", GameManager()) }
        localTyping.setOnClickListener { chooseGame("local_typing", GameManager()) }
        localLyric.setOnClickListener{ chooseGame("local_lyrics", GameManager()) }
        onlineBuzzer.setOnClickListener { chooseGame("online_buzzer", GameManager()) }
        offlineBuzzer.setOnClickListener { chooseGame("local_buzzer", GameManager(), true) }
    }



    /**
     * Set game mode. Set appropriate GameManager.
     */
    private fun chooseGame(gameMode: String, gameManager: GameManager, playOffline: Boolean = false){
        AppPreferences.setStr(getString(R.string.preferences_game_mode), gameMode)

        val bundle = bundleOf("gameManager" to gameManager)

        var nextLayout = R.id.fragment_genre_setup
        if (playOffline) {
            nextLayout = R.id.fragment_setting_setup
        }
        else if (gameMode == "online_buzzer"){
            nextLayout = R.id.fragment_choose_online_room
        }

        if(!test){
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.main_bottom_nav_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(nextLayout, bundle)
        }
    }

    /**
     * Set game genre. Fetch the data from Lastfm.
     * @param method: lastfm method to fetch songs: BY_ARTIST, BY_TAG
     * @param artist: artist to fetch songs from; used in BY_ARTIST method
     * @param tag: tag (genre) to fetch songs from: used in BY_TAG method
     * @param mode: official game mode name
     */
    private fun chooseGenre(method: String = "", artist: String = "", tag: String = "", mode: Int = 0, playOffline: Boolean = false) {
            val uri = LastfmUri()

            uri.method = method
            uri.artist = artist
            uri.tag = tag

            if (playOffline) {
                toggleViewsVisibility(
                    goneView = requireView().findViewById<LinearLayout>(R.id.chooseGame),
                    visibleView = chooseSetting
                )
            } else {
                toggleViewsVisibility(goneView = genrePerScoreboard, visibleView = chooseSetting)
            }


            gameManager.gameMode = getString(mode)
            AppPreferences.setStr(getString(R.string.preferences_game_genre), getString(mode))
            setGameSongList(uri, playOffline)
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
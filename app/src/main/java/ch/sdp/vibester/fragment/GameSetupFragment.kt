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
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.BuzzerSetupActivity
import ch.sdp.vibester.activity.ChoosePartyRoomActivity
import ch.sdp.vibester.activity.download.DownloadActivity
import ch.sdp.vibester.activity.game.LyricsBelongGameActivity
import ch.sdp.vibester.activity.game.TypingGameActivity
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
import java.io.File

/**
* Game Setup fragment with a button in the bottom navigation.
*/
@AndroidEntryPoint
class GameSetupFragment : Fragment(R.layout.fragment_layout_game_setup), AdapterView.OnItemSelectedListener {
    var difficulty = R.string.GameSetup_easy.toString()
    var game = "local_buzzer"
    var gameSize = R.string.one.toString()
    var searchArtistEditable: Editable? = null
    lateinit var gameManager: GameManager
    // TODO: OFFLINE
    private var hasInternet: Boolean = true
    private var vmGameSetup = ViewModel()
    private lateinit var genrePerScoreboard: View
    private lateinit var chooseSetting: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmGameSetup.view = view
        vmGameSetup.ctx = view.context

        genrePerScoreboard = vmGameSetup.view.findViewById<LinearLayout>(R.id.genrePerScoreboard)
        chooseSetting = vmGameSetup.view.findViewById<RelativeLayout>(R.id.chooseSetting)


        setGenreListeners()
        setGameModeListeners()

        vmGameSetup.view.findViewById<Button>(R.id.difficulty_proceed).setOnClickListener { proceedGame() }
        vmGameSetup.view.findViewById<AppCompatButton>(R.id.download).setOnClickListener {
            val newIntent = Intent(activity, DownloadActivity::class.java)
            startActivity(newIntent)
        }

        vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet).setOnClickListener { updateInternet(vmGameSetup.view.findViewById(R.id.game_setup_has_internet)) }

        updateInternet(vmGameSetup.view.findViewById<Button>(R.id.game_setup_has_internet))
        searchArtistEditable = vmGameSetup.view.findViewById<EditText>(R.id.searchArtist).text

        setReturnBtnListener()
        setSpinnerListener(R.id.difficulty_spinner, R.array.difficulties_name)
        setSpinnerListener(R.id.size_spinner, R.array.game_size_options)
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
        localBuzzer.setOnClickListener { chooseGame("local_buzzer", GameManager()) }
        localTyping.setOnClickListener { chooseGame("local_typing", GameManager()) }
        localLyric.setOnClickListener{ chooseGame("local_lyrics", GameManager()) }
        onlineBuzzer.setOnClickListener { switchToGameNoParameters(ChoosePartyRoomActivity()) }
    }


    private fun setReturnBtnListener() {
        vmGameSetup.view.findViewById<FloatingActionButton>(R.id.gameSetup_returnToMain).setOnClickListener {
            if (genrePerScoreboard.visibility == VISIBLE) {
                toggleViewsVisibility(goneView = genrePerScoreboard,
                    visibleView = vmGameSetup.view.findViewById<LinearLayout>(R.id.chooseGame))
            } else if (chooseSetting.visibility == VISIBLE) {
                toggleViewsVisibility(goneView = chooseSetting, visibleView = genrePerScoreboard)
            }
        }
    }

    private fun toggleViewsVisibility(goneView: View, visibleView:View) {
        goneView.visibility = GONE
        visibleView.visibility = VISIBLE
    }

    private fun setSpinnerListener(spinnerId: Int, resourceId: Int) {
        val spinner: Spinner = vmGameSetup.view.findViewById(spinnerId)
        val adapter = ArrayAdapter.createFromResource(
            vmGameSetup.ctx, resourceId, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        if (parent.id == R.id.difficulty_spinner) {
            difficulty = parent.getItemAtPosition(position).toString()
            when (difficulty) {
                "Easy" -> gameManager.difficultyLevel = 1
                "Medium" -> gameManager.difficultyLevel = 2
                "Hard" -> gameManager.difficultyLevel = 3
            }
        } else if (parent.id == R.id.size_spinner) {
            gameSize = parent.getItemAtPosition(position).toString()
            when (gameSize) {
                "One" -> gameManager.gameSize = 1
                "Two" -> gameManager.gameSize = 2
                "Three" -> gameManager.gameSize = 3
                "Four" -> gameManager.gameSize = 4
                "Five" -> gameManager.gameSize = 5
                "Six" -> gameManager.gameSize = 6
                "Seven" -> gameManager.gameSize = 7
                "Eight" -> gameManager.gameSize = 8
                "Nine" -> gameManager.gameSize = 9
                "Ten" -> gameManager.gameSize = 10
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        if (parent.id == R.id.difficulty_spinner) {
            difficulty = R.string.GameSetup_easy.toString()
        } else if (parent.id == R.id.size_spinner) {
            gameSize = R.string.one.toString()
        }
    }

    /**
     * Start the game based on the chosen mode
     */
    private fun proceedGame() {
        AppPreferences.setStr(getString(R.string.preferences_game_mode), this.game)

        when (this.game) {
            "local_buzzer" -> { switchToGameWithParameters(BuzzerSetupActivity()) }
            "local_typing" -> { switchToGameWithParameters(TypingGameActivity()) }
            "local_lyrics" -> { switchToGameWithParameters(LyricsBelongGameActivity()) }
        }
    }

    /**
     * Switch to an activity without any extras in intent.
     */
    private fun switchToGameNoParameters(nextActivity: AppCompatActivity) {
        val newIntent = Intent(activity, nextActivity::class.java)
        startActivity(newIntent)
    }

    /**
     * Switch to an activity with extras in intent.
     */
    private fun switchToGameWithParameters(nextActivity: AppCompatActivity) {
        val newIntent = Intent(activity, nextActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        newIntent.putExtra("Difficulty", difficulty)
        startActivity(newIntent)
    }


    /**
     * Fetch data from Lastfm and set song list in a GameManager
     * @param uri: contains all Lastfm query parameters (method, artist, tag)
     */
    private fun setGameSongList(uri: LastfmUri, playOffline: Boolean = false) {
        if (playOffline) {
            val external = vmGameSetup.ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if (external != null) {
                gameManager.setOffline(external, !playOffline)
                gameManager.setGameSongList(Gson().toJson(""), uri.method)
            }
        } else {
            val service = LastfmApiInterface.createLastfmService()
            val call = service.getSongList(uri.convertToHashmap())
            call.enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable?) {}
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    gameManager.setGameSongList(Gson().toJson(response.body()), uri.method)
                }
            })
        }
    }

    /**
     * Set game mode. Set appropriate GameManager.
     */
    private fun chooseGame(gameMode: String, gameManager: GameManager, playOffline: Boolean = false){
        this.game = gameMode
        this.gameManager = gameManager

        if (playOffline) {
            chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Personalized", mode = R.string.gameGenre_byArtistSearch, playOffline = playOffline)
        } else {
            toggleViewsVisibility(goneView = requireView().findViewById<LinearLayout>(R.id.chooseGame),
                visibleView = requireView().findViewById<ConstraintLayout>(R.id.genrePerScoreboard))
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
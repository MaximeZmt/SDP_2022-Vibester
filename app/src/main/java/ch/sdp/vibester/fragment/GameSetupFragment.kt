package ch.sdp.vibester.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.BuzzerSetupActivity
import ch.sdp.vibester.activity.ChoosePartyRoomActivity
import ch.sdp.vibester.activity.LyricsBelongGameActivity
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
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
class GameSetupFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    var difficulty = R.string.GameSetup_easy.toString()
    var game = "local_buzzer"
    var gameSize = R.string.one.toString()
    var searchArtistEditable: Editable? = null
    lateinit var gameManager: GameManager
    // TODO: OFFLINE
    private var hasInternet: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_layout_game_setup, container, false)
        val ctx = inflater.context

        view.findViewById<Button>(R.id.local_buzzer_game_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.local_typing_game_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.local_lyrics_game_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.online_buzzer_game_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.kpopButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.rockButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.btsButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.topTracksButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.imagDragonsButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.billieEilishButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.difficulty_proceed).setOnClickListener(this)
        view.findViewById<Button>(R.id.game_setup_has_internet).setOnClickListener(this)

        searchArtistEditable = view.findViewById<EditText>(R.id.searchArtist).text
        view.findViewById<Button>(R.id.validateSearch).setOnClickListener(this)

        setReturnBtnListener(view)
        setSpinnerListener(view, ctx, R.id.difficulty_spinner, R.array.difficulties_name)
        setSpinnerListener(view, ctx, R.id.size_spinner, R.array.game_size_options)
        return view
    }


    private fun setReturnBtnListener(view:View) {
        view.findViewById<FloatingActionButton>(R.id.gameSetup_returnToMain).setOnClickListener {
            if (view.findViewById<LinearLayout>(R.id.genrePerScoreboard).visibility == View.VISIBLE) {
                toggleViewsVisibility(goneView = view.findViewById<LinearLayout>(R.id.genrePerScoreboard),
                    visibleView = view.findViewById<LinearLayout>(R.id.chooseGame))
            } else if (view.findViewById<RelativeLayout>(R.id.chooseSetting).visibility == View.VISIBLE) {
                toggleViewsVisibility(goneView = view.findViewById<RelativeLayout>(R.id.chooseSetting),
                    visibleView = view.findViewById<LinearLayout>(R.id.genrePerScoreboard))
            }
        }
    }

    private fun toggleViewsVisibility(goneView: View, visibleView:View) {
        goneView.visibility = View.GONE
        visibleView.visibility = View.VISIBLE
    }

    private fun setSpinnerListener(view: View, ctx: Context, spinnerId: Int, resourceId: Int) {
        val spinner: Spinner = view.findViewById(spinnerId)
        val adapter = ArrayAdapter.createFromResource(
            ctx, resourceId, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        if (parent.id == R.id.difficulty_spinner) {
            difficulty = parent.getItemAtPosition(position).toString()
        } else if (parent.id == R.id.size_spinner) {
            gameSize = parent.getItemAtPosition(position).toString()
            when (gameSize) {
                "One" -> gameManager.setGameSize(1)
                "Two" -> gameManager.setGameSize(2)
                "Three" -> gameManager.setGameSize(3)
                "Four" -> gameManager.setGameSize(4)
                "Five" -> gameManager.setGameSize(5)
                "Six" -> gameManager.setGameSize(6)
                "Seven" -> gameManager.setGameSize(7)
                "Eight" -> gameManager.setGameSize(8)
                "Nine" -> gameManager.setGameSize(9)
                "Ten" -> gameManager.setGameSize(10)
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
    private fun setGameSongList(uri: LastfmUri) {
        val service = LastfmApiInterface.createLastfmService()
        val call = service.getSongList(uri.convertToHashmap())
        call.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable?) {}
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                // TODO: OFFLINE
                val external = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                if (external != null) {
                    gameManager.setOffline(external, hasInternet)
                }
                gameManager.setGameSongList(Gson().toJson(response.body()), uri.method)
            }
        })
    }

    /**
     * Set game mode. Set appropriate GameManager.
     */
    private fun chooseGame(gameMode: String, gameManager: GameManager){
        this.game = gameMode
        this.gameManager = gameManager

        toggleViewsVisibility(goneView = requireView().findViewById<LinearLayout>(R.id.chooseGame),
            visibleView = requireView().findViewById<ConstraintLayout>(R.id.genrePerScoreboard))
    }

    /**
     * Set game genre. Fetch the data from Lastfm.
     * @param method: lastfm method to fetch songs: BY_ARTIST, BY_TAG
     * @param artist: artist to fetch songs from; used in BY_ARTIST method
     * @param tag: tag (genre) to fetch songs from: used in BY_TAG method
     * @param mode: official game mode name
     */
    private fun chooseGenre(method: String = "", artist: String = "", tag: String = "", mode: Int = 0) {
        if (artist != "") {
            val uri = LastfmUri()

            uri.method = method
            uri.artist = artist
            uri.tag = tag

            toggleViewsVisibility(
                goneView = requireView().findViewById<LinearLayout>(R.id.genrePerScoreboard),
                visibleView = requireView().findViewById<ConstraintLayout>(R.id.chooseSetting)
            )

            gameManager.gameMode = getString(mode)
            AppPreferences.setStr(getString(R.string.preferences_game_genre), getString(mode))
            setGameSongList(uri)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.getId()) {
            R.id.local_buzzer_game_button -> chooseGame("local_buzzer", GameManager())
            R.id.local_typing_game_button -> chooseGame("local_typing", GameManager())
            R.id.local_lyrics_game_button -> chooseGame("local_lyrics", GameManager())
            R.id.online_buzzer_game_button -> switchToGameNoParameters(ChoosePartyRoomActivity())

            R.id.btsButton -> chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "BTS", mode = R.string.gameGenre_bts)
            R.id.kpopButton -> chooseGenre(method = LastfmMethod.BY_TAG.method, tag = "kpop", mode = R.string.kpop)
            R.id.imagDragonsButton -> chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Imagine Dragons", mode = R.string.gameGenre_imagine_dragons)
            R.id.rockButton-> chooseGenre(method = LastfmMethod.BY_TAG.method, tag = "rock", mode = R.string.rock)
            R.id.topTracksButton -> chooseGenre(method = LastfmMethod.BY_CHART.method, mode = R.string.top_tracks)
            R.id.billieEilishButton -> chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Billie Eilish", mode = R.string.gameGenre_billie_eilish)

            R.id.validateSearch -> chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = searchArtistEditable.toString(), mode = R.string.gameGenre_byArtistSearch)

            R.id.difficulty_proceed -> proceedGame()

            R.id.game_setup_has_internet -> switchInternet(v.findViewById(R.id.game_setup_has_internet))
        }
    }

    /**
     * Switches between Internet On and Internet Off when the view button is pressed.
     * @param view: The button responsible for internet toggling.
     */
    //TODO: OFFLINE
    private fun switchInternet(view: View) {
        val btn: Button = view as Button
        if(hasInternet) {
            hasInternet = false
            btn.text = getString(R.string.GameSetup_internetSwitchOff)
        } else {
            hasInternet = true
            btn.text = getString(R.string.GameSetup_internetSwitchOn)
        }
    }
}
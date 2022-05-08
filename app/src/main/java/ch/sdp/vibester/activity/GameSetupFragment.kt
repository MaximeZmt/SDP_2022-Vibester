package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.helper.BuzzerGameManager
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.TypingGameManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameSetupFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener{
    var difficulty = R.string.easy.toString()
    var game = "local_buzzer"
    var gameSize = R.string.one.toString()
    lateinit var gameManager: GameManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_setup_screen, container, false)
        val ctx = inflater.getContext();
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

        setReturnBtnListener(view)
        setSpinnerListener(view, ctx, R.id.difficulty_spinner, R.array.difficulties_name)
        setSpinnerListener(view, ctx, R.id.size_spinner, R.array.game_size_options)
        return view
    }


    private fun setReturnBtnListener(view:View) {
        view.findViewById<FloatingActionButton>(R.id.gameSetup_returnToMain).setOnClickListener {
            if (view.findViewById<ConstraintLayout>(R.id.chooseGenre).visibility == View.VISIBLE) {
                view.findViewById<LinearLayout>(R.id.chooseGame).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = View.GONE
            } else if (view.findViewById<ConstraintLayout>(R.id.gameSetting).visibility == View.VISIBLE) {
                view.findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.gameSetting).visibility = View.GONE
            }
        }
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
            when (difficulty) {
                "Easy"      -> setDifficultyText(R.string.difficulty_easy)
                "Medium"    -> setDifficultyText(R.string.difficulty_medium)
                "Hard"      -> setDifficultyText(R.string.difficulty_hard)
            }
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
            difficulty = R.string.easy.toString()
        } else if (parent.id == R.id.size_spinner) {
            gameSize = R.string.one.toString()
        }
    }

    /**
     * Start the game based on the chosen mode
     */
    fun proceedGame(){
        when (this.game) {
            "local_buzzer" -> { switchToGame(BuzzerSetupActivity()) }
            "local_typing" -> { switchToGame(TypingGameActivity()) }
            "local_lyrics" -> { switchToGame(LyricsBelongGameActivity()) }
        }
    }

    private fun switchToGame(nextActivity: AppCompatActivity){
        val newIntent = Intent(getActivity(), nextActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        newIntent.putExtra("Difficulty", difficulty)
        startActivity(newIntent)
    }

    private fun setDifficultyText(mode: Int) {
        requireView().findViewById<TextView>(R.id.difficulty_explanation).setText(mode)
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
                gameManager.setGameSongList(Gson().toJson(response.body()), uri.method)
            }
        })
    }

    /**
     * Choose game mode. Set appropriate GameManager.
     */
    fun chooseGame(view: View){
        when (view.id) {
            R.id.local_buzzer_game_button -> {game  = "local_buzzer"; gameManager = BuzzerGameManager() }
            R.id.local_typing_game_button -> {game = "local_typing"; gameManager = TypingGameManager()
            }
            R.id.local_lyrics_game_button -> {game = "local_lyrics"; gameManager = GameManager()}
            R.id.online_buzzer_game_button -> {game = "online_buzzer"; gameManager = GameManager(); switchToGame(ChoosePartyRoomActivity())}
        }
        requireView().findViewById<LinearLayout>(R.id.chooseGame).visibility = View.GONE
        requireView().findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = View.VISIBLE
    }

    /**
     * Choose genre for the game. Call function to fetch the data from Lastfm.
     */
    fun chooseGenre(view: View) {
        var method =  ""
        var artist = ""
        var tag = ""
        var mode = ""
        val uri = LastfmUri()
        when (view.id) {
            R.id.btsButton -> {method = LastfmMethod.BY_ARTIST.method; artist = "BTS"; mode = "BTS" }
            R.id.kpopButton -> {method = LastfmMethod.BY_TAG.method; tag = "kpop"; mode = "kpop" }
            R.id.imagDragonsButton -> {method = LastfmMethod.BY_ARTIST.method; artist = "Imagine Dragons"; mode = "Imagine Dragons"}
            R.id.rockButton-> {method = LastfmMethod.BY_TAG.method; tag = "rock"; mode = "rock" }
            R.id.topTracksButton -> {method = LastfmMethod.BY_CHART.method; mode = "top tracks"}
            R.id.billieEilishButton -> {method = LastfmMethod.BY_ARTIST.method; artist = "Billie Eilish"; mode = "billie eilish"}
        }
        uri.method = method
        uri.artist = artist
        uri.tag = tag

        requireView().findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = View.GONE
        requireView().findViewById<ConstraintLayout>(R.id.gameSetting).visibility = View.VISIBLE

        gameManager.gameMode = mode
        setGameSongList(uri)
    }

    override fun onClick(v: View?) {
        when(v!!.getId()) {
            R.id.local_buzzer_game_button -> chooseGame(v)
            R.id.local_typing_game_button -> chooseGame(v)
            R.id.local_lyrics_game_button -> chooseGame(v)
            R.id.online_buzzer_game_button -> chooseGame(v)
            R.id.difficulty_proceed -> proceedGame()
            else -> chooseGenre(v)
        }
    }


}
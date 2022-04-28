package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.helper.TypingGameManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class to choose game mode, genre and difficulty.
 */
class GameSetupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var difficulty = "Easy"
    var game = "local_buzzer"
     lateinit var gameManager: GameManager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_game_setup_screen)
        val ctx: Context = this

        val retButton: FloatingActionButton = findViewById(R.id.gameSetup_returnToMain)

        retButton.setOnClickListener {
            if(findViewById<LinearLayout>(R.id.chooseGame).visibility == VISIBLE){
                IntentSwitcher.switchBackToWelcome(this)
                finish()
            }else if (findViewById<ConstraintLayout>(R.id.chooseGenre).visibility == VISIBLE){
                findViewById<LinearLayout>(R.id.chooseGame).visibility = VISIBLE
                findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = GONE
            }else if (findViewById<ConstraintLayout>(R.id.chooseDifficulty).visibility == VISIBLE){
                findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = VISIBLE
                findViewById<ConstraintLayout>(R.id.chooseDifficulty).visibility = GONE
            }
        }


        val spinnerDifficulty: Spinner = findViewById(R.id.difficulty_spinner)
        val adapter = ArrayAdapter.createFromResource(
            ctx,
            R.array.difficulties_name,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficulty.adapter = adapter
        spinnerDifficulty.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        difficulty = parent.getItemAtPosition(position).toString()
        when(difficulty) {
            "Easy"      -> setDifficultyText(R.string.difficulty_easy)
            "Medium"    -> setDifficultyText(R.string.difficulty_medium)
            "Hard"      -> setDifficultyText(R.string.difficulty_hard)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {difficulty = "Easy"}

    /**
     * Start the game based on the chosen mode
     */
    fun proceedGame(view: View){
         if(this.game == "local_buzzer"){
             switchToGame(BuzzerSetupActivity())
         }
         else if(this.game == "local_typing"){
             switchToGame(TypingGameActivity())
         }
         else if(this.game == "local_lyrics"){
            switchToGame(LyricsBelongGameActivity())
        }
    }

    private fun switchToGame(nextActivity: AppCompatActivity){
        val newIntent = Intent(this, nextActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        newIntent.putExtra("Difficulty", difficulty)
        startActivity(newIntent)
        finish()
    }

    private fun setDifficultyText(mode: Int) {
        findViewById<TextView>(R.id.difficulty_explanation).setText(mode)
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
            R.id.local_buzzer_game_button -> {game  = "local_buzzer"; gameManager = GameManager()}
            R.id.local_typing_game_button -> {game = "local_typing"; gameManager = TypingGameManager()}
            R.id.local_lyrics_game_button -> {game = "local_lyrics"; gameManager = GameManager()}
        }
        findViewById<LinearLayout>(R.id.chooseGame).visibility = GONE
        findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = VISIBLE
    }

    /**
     * Choose genre for the game. Call function to fetch the data from Lastfm.
     */
    fun chooseGenre(view: View) {
        var method =  ""
        var artist = ""
        var tag = ""
        val uri = LastfmUri()
        when (view.id) {
            R.id.btsButton -> {method = LastfmMethod.BY_ARTIST.method; artist = "BTS" }
            R.id.kpopButton -> {method = LastfmMethod.BY_TAG.method; tag = "kpop" }
            R.id.imagDragonsButton -> {method = LastfmMethod.BY_ARTIST.method; artist = "Imagine Dragons"}
            R.id.rockButton-> {method = LastfmMethod.BY_TAG.method; tag = "rock" }
            R.id.topTracksButton -> {method = LastfmMethod.BY_CHART.method}
            R.id.billieEilishButton -> {method =LastfmMethod.BY_ARTIST.method; artist = "Billie Eilish"}
        }
        uri.method = method
        uri.artist = artist
        uri.tag = tag

        findViewById<ConstraintLayout>(R.id.chooseGenre).visibility = GONE
        findViewById<ConstraintLayout>(R.id.chooseDifficulty).visibility = VISIBLE

        setGameSongList(uri)
    }

}

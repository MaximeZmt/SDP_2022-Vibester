package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.helper.GameManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameSetupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var difficulty = "Easy"
    var game = "local_buzzer"
    val gameManager = GameManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_game_setup_screen)

        gameToGenre(R.id.local_buzzer_game_button,
            findViewById<LinearLayout>(R.id.chooseGame),
            findViewById<ConstraintLayout>(R.id.chooseGenre), "local_buzzer")

        gameToGenre(R.id.local_typing_game_button,
            findViewById<LinearLayout>(R.id.chooseGame),
            findViewById<ConstraintLayout>(R.id.chooseGenre), "local_typing")


        val spinnerDifficulty: Spinner = findViewById(R.id.difficulty_spinner)
        initSpinner(spinnerDifficulty, R.array.difficulties_name)
    }


    private fun initSpinner(spinner: Spinner, spinner_array: Int) {
        ArrayAdapter.createFromResource(
            this,
            spinner_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when(difficulty) {
            "Easy"      -> setDifficultyText(R.string.difficulty_easy)
            "Medium"    -> setDifficultyText(R.string.difficulty_medium)
            "Hard"      -> setDifficultyText(R.string.difficulty_hard)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {difficulty = "Easy"}

     fun proceedGame(view:View){
         if(this.game == "local_buzzer"){
             val newIntent = Intent(this, BuzzerSetupActivity::class.java)
             newIntent.putExtra("gameManager", gameManager)
             newIntent.putExtra("difficulty", difficulty)
             startActivity(newIntent)
         }
         else if(this.game == "local_typing"){
             val newIntent = Intent(this, TypingGameActivity::class.java)
             newIntent.putExtra("gameManager", gameManager)
             newIntent.putExtra("difficulty", difficulty)
             startActivity(newIntent)
         }
    }

    private fun gameToGenre(buttonId: Int, currentLayout: ViewGroup, nextLayout: ViewGroup, game: String) {
        val btn = findViewById<Button>(buttonId)
        btn.setOnClickListener {
            currentLayout.visibility = GONE
            nextLayout.visibility = VISIBLE
            this.game = game
        }
    }

    private fun genreToDifficulty(currentLayout: ViewGroup, nextLayout: ViewGroup) {
        currentLayout.visibility = GONE
        nextLayout.visibility = VISIBLE
    }

    private fun setDifficultyText(mode: Int) {
        findViewById<TextView>(R.id.difficulty_explanation).setText(mode)
    }


    /**
     * Fetch data from Lastfm and show song list in a ListView
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


    fun play(view: View) {
        var uri = LastfmUri()
        when (view.getId()) {
            R.id.btsButton -> {uri.method =LastfmMethod.BY_ARTIST.method; uri.artist = "BTS" }
            R.id.kpopButton -> {uri.method =LastfmMethod.BY_TAG.method; uri.tag = "kpop" }
            R.id.imagDragonsButton -> {uri.method =LastfmMethod.BY_ARTIST.method; uri.artist = "Imagine Dragons"}
            R.id.rockButton-> {uri.method =LastfmMethod.BY_TAG.method; uri.tag = "rock" }
            R.id.topTracksButton -> {uri.method = LastfmMethod.BY_CHART.method}
            R.id.billieEilishButton -> {uri.method =LastfmMethod.BY_ARTIST.method; uri.artist = "Billie Eilish"}
        }

        genreToDifficulty(findViewById<ConstraintLayout>(R.id.chooseGenre),
            findViewById<ConstraintLayout>(R.id.chooseDifficulty))
        setGameSongList(uri)
    }

}

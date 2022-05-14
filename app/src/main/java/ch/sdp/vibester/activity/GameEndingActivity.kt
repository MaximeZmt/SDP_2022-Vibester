package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.model.SongListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A class representing the activity which appears upon
 * completion of a game. Shows various stats.
 */
class GameEndingActivity : AppCompatActivity() {

    private var incorrectSongList: ArrayList<String> = arrayListOf()
    private var correctSongList: ArrayList<String> = arrayListOf()
    private var statNames: ArrayList<String> = arrayListOf()
    private var statValues: ArrayList<String> = arrayListOf()

    lateinit var songListAdapter: SongListAdapter
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        if (AppPreferences.gameMode == "local_typing" || AppPreferences.gameMode == "local_lyrics") {
            setContentView(R.layout.activity_end_solo)
            getFromIntentSolo(intent)
        }
        else{
            setContentView(R.layout.activity_end_multiple)
            getFromIntentMultiple(intent)
        }

        setGameMode()

        recyclerView = findViewById(R.id.end_song_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        songListAdapter = SongListAdapter(incorrectSongList, correctSongList)
        recyclerView!!.adapter = songListAdapter

        findViewById<FloatingActionButton>(R.id.end_back_to_welcome)
            .setOnClickListener {
                IntentSwitcher.switch(this, MainActivity::class.java)
            }
    }

    /**
     * Set text for game mode
     */
    private fun setGameMode(){
        val gameMode = AppPreferences.gameMode?.replace("_", " ")?.replaceFirstChar { it.uppercase() }
        val gameGenre = AppPreferences.gameGenre
        findViewById<TextView>(R.id.end_game_mode).text = gameMode + " - " + gameGenre
    }

    /**
     * Set statistics for Solo Game
     */
    private fun setSoloStats() {
        val stat1: TextView = findViewById(R.id.end_stat1)
        stat1.text = statNames.get(0)
        val stat1res: TextView = findViewById(R.id.end_stat1_res)
        stat1res.text = statValues.get(0)
    }

    /**
     * Handle intent values for solo game
     * @param intent: intent received by the activity
     */
    private fun getFromIntentSolo(intent: Intent) {
        incorrectSongList = intent.getStringArrayListExtra("incorrectSongList") as ArrayList<String>
        correctSongList = intent.getStringArrayListExtra("correctSongList") as ArrayList<String>

        statNames = intent.getStringArrayListExtra("statNames") as ArrayList<String>
        statValues = intent.getStringArrayListExtra("statValues") as ArrayList<String>
        setSoloStats()
    }

    /**
     * Handle intent values for multiple players game
     * @param intent: intent received by the activity
     */
    private fun getFromIntentMultiple(intent: Intent) {
        if (intent.hasExtra("Winner Name")) {
            val winner = intent.getStringExtra("Winner Name")
            findViewById<TextView>(R.id.winnerText).text = winner
        }
    }
}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TableLayout
import android.widget.TableRow
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
 * Game ending activity with game stats and list of songs quessed correctly/wrong
 */
class GameEndingActivity : AppCompatActivity() {

    private val endStatArrayList = arrayListOf<Int>(R.id.end_stat1, R.id.end_stat2, R.id.end_stat3, R.id.end_stat4)

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

        val game_mode = AppPreferences.getStr(getString(R.string.preferences_game_mode))
        if (game_mode == "local_typing" || game_mode == "local_lyrics") {
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

        findViewById<FloatingActionButton>(R.id.end_returnToMain)
            .setOnClickListener {
                IntentSwitcher.switch(this, MainActivity::class.java)
            }
    }

    /**
     * Set text for game mode
     */
    private fun setGameMode(){
        val gameMode = AppPreferences.getStr(getString(R.string.preferences_game_mode))?.replace("_", " ")?.replaceFirstChar { it.uppercase() }
        val gameGenre = AppPreferences.getStr(getString(R.string.preferences_game_genre))
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
     * Creates a text view with a given text and gravity
     * @param text: the text to be put in the view
     * @param gravity: the desired gravity of the view
     * @return the created view
     */
    private fun createTextView(text: String, gravity: Int, row: TableRow) {
        val view = TextView(this)
        view.text = text
        view.height = 100
        view.width = 300
        view.textAlignment = View.TEXT_ALIGNMENT_CENTER
        view.fontFeatureSettings = "monospace"
        view.textSize = 20f
        view.gravity = gravity
        row.addView(view)
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

        if (intent.hasExtra("Player Scores")) {
            val playerScores =
                intent.getSerializableExtra("Player Scores")!! as HashMap<String, Int>
            var i = 0
            for (pName in playerScores.keys) {
                val row = findViewById<TableRow>(endStatArrayList[i])
                row.visibility = View.VISIBLE

                createTextView(pName, Gravity.LEFT, row)
                createTextView(playerScores[pName]!!.toString(), Gravity.RIGHT, row)

                i += 1
            }
        }
    }
}
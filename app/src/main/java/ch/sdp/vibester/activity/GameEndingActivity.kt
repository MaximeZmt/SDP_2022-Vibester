package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.download.DownloadFunctionalityActivity
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.model.SongListAdapterForEndGame
import ch.sdp.vibester.user.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Game ending activity with game stats and list of songs quessed correctly/wrong
 */
class GameEndingActivity : DownloadFunctionalityActivity(), OnItemClickListener {

    private val endStatArrayList =
        arrayListOf(R.id.end_stat0, R.id.end_stat1, R.id.end_stat2, R.id.end_stat3)

    private val nameIds = arrayListOf(R.id.name_0, R.id.name_1, R.id.name_2, R.id.name_3)
    private val scoreIds = arrayListOf(R.id.score_0, R.id.score_1, R.id.score_2, R.id.score_3)

    private var incorrectSongList: ArrayList<String> = arrayListOf()
    private var correctSongList: ArrayList<String> = arrayListOf()
    private var statNames: ArrayList<String> = arrayListOf()
    private var statValues: ArrayList<String> = arrayListOf()

    private lateinit var songListAdapter: SongListAdapterForEndGame
    private var recyclerView: RecyclerView? = null

    /**
     * Generic onCreate method. Nothing of interested here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val gameMode = AppPreferences.getStr(getString(R.string.preferences_game_mode))
        if (gameMode == "local_typing" || gameMode == "local_lyrics") { //|| gameMode == "local_buzzer"
            setContentView(R.layout.activity_end_solo)
            getFromIntentSolo(intent)
        } else {
            setContentView(R.layout.activity_end_multiple)
            getFromIntentMultiple(intent)
        }

        setGameMode()

        setUpRecyclerView()

        val returnBtn = findViewById<FloatingActionButton>(R.id.end_returnToMain)
        Helper().setReturnToMainListener(returnBtn, this)

        createDownloadReceiver(null)
    }

    /**
     * Sets up the recycler view which hosts the multitude of songs that were in the previously played
     * game session.
     */
    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.end_song_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        songListAdapter = SongListAdapterForEndGame(incorrectSongList, correctSongList, this)
        recyclerView!!.adapter = songListAdapter
    }

    /**
     * Set text for game mode
     */
    private fun setGameMode() {
        val gameMode =
            AppPreferences.getStr(getString(R.string.preferences_game_mode))?.replace("_", " ")?.replaceFirstChar { it.uppercase() }
        val gameGenre = AppPreferences.getStr(getString(R.string.preferences_game_genre))
        findViewById<TextView>(R.id.end_game_mode).text = "$gameMode - $gameGenre"
    }

    /**
     * Set statistics for Solo Game
     */
    private fun setSoloStats() {
        val stat1: TextView = findViewById(R.id.end_stat1)
        stat1.text = statNames[0]
        val stat1res: TextView = findViewById(R.id.end_stat1_res)
        stat1res.text = statValues[0]
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
     * Puts the given text in the text view
     * @param text: the text to be put in the view
     * @return the created view
     */
    private fun setupTextView(view: Int, text: String) {
        findViewById<TextView>(view).text = text
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

                setupTextView(nameIds[i], pName)
                setupTextView(scoreIds[i], playerScores[pName]!!.toString())

                i += 1
            }
        }

        if (intent.hasExtra("incorrectSongList") && intent.hasExtra("correctSongList")){
            incorrectSongList = intent.getStringArrayListExtra("incorrectSongList") as ArrayList<String>
            correctSongList = intent.getStringArrayListExtra("correctSongList") as ArrayList<String>

            incorrectSongList.iterator().forEach { Log.e("ERR", it) }
            correctSongList.iterator().forEach { Log.e("CORR", it) }
            //Log.e("ERR", incorrectSongList)
        }


    }

    /**
     * Handles the case where an item in the recycler view is clicked, i.e the download buttons.
     * @param position: The position of the clicked song in the list of songs.
     */
    override fun onItemClick(position: Int) {
        //Retrieval of the top-most parent which is the RecyclerView
        val parentActual = findViewById<Button>(R.id.song_download).parent as RelativeLayout
        val parentOfParent = parentActual.parent as RecyclerView
        Log.d("parent name of button is ============================ ", "${parentOfParent.javaClass}")
        Log.d("parent's number of children is ============================ ", "${parentOfParent.childCount}")

        //Retrieve the RelativeLayout that hosts its children, and those children
        val relative = parentOfParent.children.elementAt(position) as RelativeLayout
        val children = relative.children

        //Retrieve items of the child that is currently being selected by the position argument
        val songName = children.elementAt(0) as TextView
        val downloadButton = children.elementAt(1) as Button
        val downloadOngoing = children.elementAt(3) as ProgressBar
        val downloadDone = children.elementAt(2) as ImageView
        Log.d("child song is =============================== ", "${songName.text}")
        Log.d("child song is =============================== ", "${downloadButton.id}")
        Log.d("child song is =============================== ", "${downloadOngoing.id}")

        //Logic to switch item visibility and start the download attempt
        val songList = incorrectSongList + correctSongList
        if (downloadComplete) {
            Log.d("-----------------------------", "++++++++++++++++++++++++++++++++++++")
            downloadButton.visibility = View.INVISIBLE
            downloadOngoing.visibility = View.VISIBLE
            Log.d("name of button is ============================ ", "${downloadButton.id}")
            Log.d("name of ongoing is ============================ ", "${downloadOngoing.id}")
        }
        downloadListener(null, songList[position])
    }


}
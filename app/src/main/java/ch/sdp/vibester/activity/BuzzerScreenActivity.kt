package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import ch.sdp.vibester.BuzzerScoreUpdater
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.BuzzerGameManager
import ch.sdp.vibester.model.Song
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BuzzerScreenActivity : GameActivity() {

    private val artworkDim = 200
    private val noBuzzerPressed = -1
    private val buzzersToRows:HashMap<Int, Int> = initHashmap()
    private val rowsIdArray = ArrayList(buzzersToRows.values)
    private val buzIds = ArrayList(buzzersToRows.keys)
    private var winnerId = -1 // same function as the winnerId in the updater. Ugly placeholder solution for now
    private lateinit var gameManager: BuzzerGameManager
    private var gameIsOn: Boolean = true

    private fun initHashmap(): HashMap<Int, Int> {
        val buzzersToRows:HashMap<Int, Int> = hashMapOf()
        buzzersToRows.put(R.id.buzzer_0, R.id.row_0)
        buzzersToRows.put(R.id.buzzer_1, R.id.row_1)
        buzzersToRows.put(R.id.buzzer_2, R.id.row_2)
        buzzersToRows.put(R.id.buzzer_3, R.id.row_3)
        return buzzersToRows
    }
    var pressedBuzzer = noBuzzerPressed

    private fun setPressed(id: Int) {
        pressedBuzzer = id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_buzzer_screen)

        val ctx: Context = this

        val getIntent = intent.extras
        if (getIntent != null) {

            val nPlayers = getIntent.getInt("Number of players")

            val answer = findViewById<LinearLayout>(R.id.answer)
            answer.visibility=View.INVISIBLE

            val allPoints = Array(nPlayers, { i -> 0 })
            val playersFull = getIntent.getStringArray("Player Names")
            val players = nPlayers.let { playersFull?.copyOfRange(0, it) }

            val updater = BuzzerScoreUpdater(buzIds, allPoints)

            if (players != null) {
                buildScores(players, allPoints)
                buildBuzzers(players, answer)
            }
            setAnswerButton(ctx, answer, findViewById(R.id.buttonCorrect), updater, buzzersToRows)
            setAnswerButton(ctx, answer, findViewById(R.id.buttonWrong), updater, buzzersToRows)

            // null pointer?
            if (getIntent.getSerializable("gameManager") != null) {
                gameManager = getIntent.getSerializable("gameManager") as BuzzerGameManager
                gameManager.scoreUpdater = updater
            }

            startFirstRound(ctx, gameManager)
        }
    }

    /**
     * A custom onDestroy to verify progressbar and media player are stopped
     */
    override fun onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
        if (this::gameManager.isInitialized && gameManager.initializeMediaPlayer()) {
            gameManager.stopMediaPlayer()
        }
        super.onDestroy()
    }

    /**
     * Function to set a song for the first round and play a game.
     */
    private fun startFirstRound(ctx: Context, gameManager: BuzzerGameManager){
        if (!isEndGame(gameManager)) {
            startRound(ctx, gameManager)
        }
        else{
            switchToEnding(gameManager)
        }
    }

    /**
     * Function to set a new round. It includes reinitializing activity elements,
     * and playing new song for the round.
     */
    private fun startRound(ctx: Context, gameManager: BuzzerGameManager) {
        gameIsOn = true
        findViewById<LinearLayout>(R.id.answer).visibility=View.INVISIBLE
        val title = gameManager.getCurrentSong().getTrackName()
        val artist = gameManager.getCurrentSong().getArtistName()
        findViewById<TextView>(R.id.songTitle).text= "$title - $artist"
        Glide.with(ctx).load(gameManager.getCurrentSong().getArtworkUrl()).override(artworkDim, artworkDim).into(findViewById(R.id.songArtwork))
        gameManager.playSong()
        checkRunnable()
        barTimer(findViewById(R.id.progressBarBuzzer), ctx, gameManager)
    }

    /**
     * Custom handle of the bar progress.
     */
    private fun barTimer(myBar: ProgressBar, ctx:Context, gameManager: BuzzerGameManager){
        initializeBarTimer(myBar)
        runnable = object : Runnable {
            override fun run() {
                if (myBar.progress > 0) {
                    decreaseBarTimer(myBar)
                    handler.postDelayed(this, 999) //just a bit shorter than a second for safety
                } else if (myBar.progress == 0) {
                    if (gameManager.playingMediaPlayer()) {
                        gameManager.stopMediaPlayer()
                    }
                    checkAnswer(ctx, null, gameManager)
                }
            }
        }
        handler.post(runnable!!)
    }

    /**
     * Generate a change of intent at the end of a game
     */
    fun checkAnswer(ctx: Context, chosenSong: Song?, gameManager: BuzzerGameManager) {
        val playedSong = gameManager.getCurrentSong()

        if (chosenSong != null && chosenSong.getTrackName() == playedSong.getTrackName() && chosenSong.getArtistName() == playedSong.getArtistName()) {
            gameManager.increaseScore()
            gameManager.addCorrectSong()
        } else {
            gameManager.addWrongSong()
        }
        endRound(gameManager)
    }

    /**
     * Function called in the end of each round. Displays the button "Next" and
     * sets the next songs to play.
     */
     fun endRound(gameManager: GameManager){
        gameIsOn = false
        super.endRound(gameManager, this::testWinner)
    }

    fun testWinner() {
        gameManager.scoreUpdater.getWinnerId()
    }

    /**
     * Programmatically builds the table of scores according to the number of players
     * @param players: an array of player names
     * @param allPoints: an array of points/scores, all set to 0 before the function is called
     */
    private fun buildScores(players: Array<String>, allPoints: Array<Int>) {

        val scores = findViewById<LinearLayout>(R.id.scoresTable)
        val viewsOfPoints = arrayOfNulls<TableRow>(players.size)

        var i = 0

        for (pName in players) {

            val score = TableRow(this)
            score.gravity = Gravity.CENTER

            val nameView = TextView(this)
            nameView.text = pName
            nameView.height = 75
            nameView.width = 300
            nameView.gravity = Gravity.LEFT

            val points = TextView(this)
            points.text = allPoints[i].toString()
            points.height = 75
            points.width = 150
            points.gravity = Gravity.RIGHT
            points.id=rowsIdArray[i]

            score.addView(nameView)
            score.addView(points)
            viewsOfPoints.set(i, score)
            scores.addView(score)

            i = i + 1
        }
    }

    /**
     * Programmatically builds the buzzers according to the number and names of players.
     * @param players: an array of player names
     * @param answer: the answer layout
     */
    private fun buildBuzzers(players: Array<String>, answer: LinearLayout) {

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)
        val buttons = arrayOfNulls<Button>(players.size)

        var i = 0

        for (pName in players) {
            val button = Button(this)
            button.id = buzIds[i]
            button.text = pName
            button.width = 100
            button.height = 0
            buttons.set(i, button)
            button.setOnClickListener {
                answer.visibility = android.view.View.VISIBLE
                setPressed(button.id)
            }
            buzzers.addView(button)
            i = i + 1
        }
    }

    /**
     * Connects the answer buttons to the answer layout's visibility
     * @param answer: the answer layout
     * @param button: the answer button to be set
     * @param updater: the updater for the scores
     * @param map: a map from the buzzers' IDs to the IDs of each score's position in the score table layout
     */
    private fun setAnswerButton(ctx: Context, answer: LinearLayout, button: Button, updater: BuzzerScoreUpdater, map: Map<Int, Int>) {
        button.setOnClickListener {
            answer.visibility = android.view.View.INVISIBLE
            if (pressedBuzzer >= 0) {
                if(button.id==R.id.buttonCorrect)  {
                    updater.updateScoresArray(pressedBuzzer, 1)
                    winnerId = updater.getWinnerId()
                } else {updater.updateScoresArray(pressedBuzzer, -1)}
                val view = map[pressedBuzzer]?.let { it1 -> findViewById<TextView>(it1) }
                if (view != null && updater.getMap().keys.contains(pressedBuzzer)) {view.text=updater.getMap()[pressedBuzzer].toString()}
            }
            if (gameManager.playingMediaPlayer()) {
                gameManager.stopMediaPlayer()
            }
            setPressed(noBuzzerPressed) // reset the buzzer
            gameManager.setNextSong()
            startRound(ctx, gameManager)
        }

    }


    /**
     * Fires an intent from the Gamescreen to the Ending Screen
     */
    fun switchToEnding(view: View) {
        if (gameManager.playingMediaPlayer()) {
            gameManager.stopMediaPlayer()
        }
        val mockArray = arrayListOf<String>("One", "Two", "Three", "Four", "Five")
        val intent = Intent(this, GameEndingActivity::class.java)

        val incArray: ArrayList<String> = mockArray
        val statNames: ArrayList<String> = mockArray
        val statVal: ArrayList<String> = mockArray

        intent.putExtra("playerName", "Arda")
        intent.putExtra("nbIncorrectSong", 3)

        intent.putExtra("Winner Name", if (winnerId>0) {findViewById<Button>(winnerId).text} else {null})

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }
}
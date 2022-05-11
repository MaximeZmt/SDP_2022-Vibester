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
import ch.sdp.vibester.model.Song
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BuzzerScreenActivity : GameActivity() {

    private val artworkDim = 200
    private val noBuzzerPressed = -1
    private val buzzersToRows:HashMap<Int, Int> = initHashmap()

    /* the array must be declared explicitly (and not with buzzersToRows.keys)
    else the buzzers may not be ordered properly
     */
    private val buzIds = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)

    private lateinit var gameManager: GameManager
    private lateinit var scoreUpdater: BuzzerScoreUpdater
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
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_buzzer_screen)

        val ctx: Context = this

        val intentExtras = intent.extras
        if (intentExtras != null) {
            super.setMax(intent)
            val nPlayers = intentExtras.getInt("Number of players")
            val allPoints = Array(nPlayers, { i -> 0 })
            val players = nPlayers.let { intentExtras.getStringArray("Player Names")?.copyOfRange(0, it) }

            if (intentExtras.getSerializable("gameManager") != null) {
                gameManager = intentExtras.getSerializable("gameManager") as GameManager
            }

            scoreUpdater = BuzzerScoreUpdater(buzIds, allPoints)

            if (players != null) {
                buildScores(players, allPoints)
                buildBuzzers(players, findViewById(R.id.answer))
            }
            setAnswerButton(ctx, findViewById(R.id.buttonCorrect), buzzersToRows)
            setAnswerButton(ctx, findViewById(R.id.buttonWrong), buzzersToRows)
            setNextButton(ctx, gameManager)
            super.startFirstRound(ctx, gameManager, ::startRoundBuzzer)
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
     * Function to set a new round. It includes reinitializing activity elements,
     * and playing new song for the round.
     */
    fun startRoundBuzzer(ctx: Context, gameManager: GameManager) {
        gameIsOn = true
        findViewById<LinearLayout>(R.id.answer).visibility=View.INVISIBLE
        val title = gameManager.getCurrentSong().getTrackName()
        val artist = gameManager.getCurrentSong().getArtistName()
        findViewById<TextView>(R.id.songTitle).text= "$title - $artist"
        Glide.with(ctx).load(gameManager.getCurrentSong().getArtworkUrl()).override(artworkDim, artworkDim).into(findViewById(R.id.songArtwork))
        gameManager.playSong()
        checkRunnable()
        super.barTimer(findViewById(R.id.progressBarBuzzer), ctx, gameManager, ::checkAnswer)
    }

    /**
     * Generate a change of intent at the end of a game
     */
    private fun checkAnswer(ctx: Context, chosenSong: Song?, gameManager: GameManager) {
        val playedSong = gameManager.getCurrentSong()
        if (checkSong(chosenSong, playedSong)) {
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
        scoreUpdater.computeWinner()
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
            points.id= buzzersToRows[buzIds[i]]!!

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

        val buttons = arrayOfNulls<Button>(players.size)

        var i = 0

        for (pName in players) {
            val button = findViewById<Button>(buzIds[i])
            button.text = pName
            button.visibility=View.VISIBLE
            buttons.set(i, button)
            button.setOnClickListener {
                if (findViewById<ProgressBar>(R.id.progressBarBuzzer).progress>0 && findViewById<Button>(R.id.nextSongBuzzer).visibility==View.GONE) {
                    answer.visibility = android.view.View.VISIBLE
                    setPressed(button.id)
                }
            }
            i = i + 1
        }
    }

    /**
     * Connects the answer buttons to the answer layout's visibility
     * @param answer: the answer layout
     * @param button: the answer button to be set
     * @param scoreUpdater: the updater for the scores
     * @param map: a map from the buzzers' IDs to the IDs of each score's position in the score table layout
     */
    private fun setAnswerButton(ctx: Context, button: Button, map: Map<Int, Int>) {
        val answer = findViewById<LinearLayout>(R.id.answer)
        button.setOnClickListener {
            answer.visibility = android.view.View.INVISIBLE
            if (pressedBuzzer >= 0) {
                if(button.id==R.id.buttonCorrect)  {
                    scoreUpdater.updateScoresArray(pressedBuzzer, true)
                } else {scoreUpdater.updateScoresArray(pressedBuzzer, false)}
                val view = map[pressedBuzzer]?.let { it1 -> findViewById<TextView>(it1) }
                if (view != null && scoreUpdater.getMap().keys.contains(pressedBuzzer)) {view.text=scoreUpdater.getMap()[pressedBuzzer].toString()}
            }
            if (gameManager.playingMediaPlayer()) {
                gameManager.stopMediaPlayer()
            }
            setPressed(noBuzzerPressed) // reset the buzzer
            gameManager.setNextSong()
            toggleBtnVisibility(R.id.nextSongBuzzer, true)
        }

    }

    private fun setNextButton(ctx: Context, gameManager: GameManager) {
        findViewById<Button>(R.id.nextSongBuzzer).setOnClickListener {
            toggleBtnVisibility(R.id.nextSongBuzzer, false)
            startRoundBuzzer(ctx, gameManager)
        }
    }

    fun prepareWinnerAnnouncement(scoreUpdater: BuzzerScoreUpdater): String {
        val winner: ArrayList<Int> = scoreUpdater.computeWinner()
        val winnerAnnouncement: String
        when (winner.size) {
            0 -> winnerAnnouncement = getString(R.string.BuzzerScreen_noWinner)
            1 -> winnerAnnouncement = getString(R.string.BuzzerScreen_oneWinner) + findViewById<Button>(winner.get(0)).text
            else -> {
                var winnersAnd = ""
                for (id in winner) {
                    winnersAnd = winnersAnd.plus(findViewById<Button>(id).text)
                    if (id!=winner.get(winner.size-1)) {
                        winnersAnd = winnersAnd.plus( " and ")
                    }
                }
                winnerAnnouncement = getString(R.string.BuzzerScreen_moreThanOneWinner) + winnersAnd
            }
        }
        return winnerAnnouncement
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

        intent.putExtra("Winner Name", prepareWinnerAnnouncement(scoreUpdater))

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }
}
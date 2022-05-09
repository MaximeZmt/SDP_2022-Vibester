package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.*
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LyricAPI
import ch.sdp.vibester.api.LyricAPI_CreateLyricServiceFactory.createLyricService
import ch.sdp.vibester.api.LyricsOVHApiInterface
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Lyric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Game checks if the player say the lyrics of the given song correct
 */
class LyricsBelongGameActivity : GameActivity() {
    private lateinit var gameManager: GameManager

    private val requestAudio = 100
    private var speechInput = "-1"
    private lateinit var lyrics: String
    private lateinit var songName: String
    private lateinit var artistName: String

    /**
     * Generic onCreate method, belonging to the LyricsBelongGameActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_belong_game)

        val ctx: Context = this
        val getIntent = intent.extras
        if (getIntent != null) {
            gameManager = getIntent.getSerializable("gameManager") as GameManager
            findViewById<Button>(R.id.nextSongButton).setOnClickListener {
                startRound(ctx, gameManager)
            }
            findViewById<Button>(R.id.lyricMatchButton).setOnClickListener {
                getAndCheckLyrics(ctx, songName, artistName, speechInput, gameManager)
            }
            gameManager.setNextSong()
            startRound(ctx, gameManager)
            super.setMax(intent)
        }

        findViewById<ImageView>(R.id.btnSpeak).setOnClickListener {
            getSpeechInput()
        }
    }

    /**
     * Function to claim speech input.
     */
    private fun getSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, requestAudio) //deprecated but works
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data) //deprecated but works
        if (requestCode == requestAudio || data != null) {
            val res = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            speechInput = res?.get(0) ?: "Didn't catch"
            updateSpeechResult(speechInput)
        }
    }

    /**
     * Function to set a new round. It includes reinitializing activity elements,
     * and setting a new song for the round.
     *
     * @param ctx: Context on which the round is happening.
     * @param gameManager: The gameManager instance that is managing the current game.
     */
    private fun startRound(ctx: Context, gameManager: GameManager) {
        toggleBtnVisibility(R.id.lyricMatchButton, false)
        toggleBtnVisibility(R.id.nextSongButton, false)
        songName = gameManager.currentSong.getTrackName()
        artistName = gameManager.currentSong.getArtistName()

        val frameLay = findViewById<FrameLayout>(R.id.LyricsSongQuestion)
        frameLay.removeAllViews()
        frameLay.addView(showSongAndImage(gameManager.currentSong, this@LyricsBelongGameActivity))

        checkRunnable()
        barTimer(ctx, findViewById(R.id.progressBarLyrics))
    }

    override fun endRound(gameManager: GameManager, callback: (() -> Unit)?) {
        super.endRound(gameManager, this::setScores)
        toggleBtnVisibility(R.id.nextSongButton, true)
    }

    /**
     * Displays the given String in lyricResult TextView and renders the check button visible.
     * @param speechInput: The string to display
     */
    private fun updateSpeechResult(speechInput: String) {
        findViewById<TextView>(R.id.lyricResult).text = speechInput
        findViewById<Button>(R.id.lyricMatchButton).visibility = View.VISIBLE
    }

    /**
     * Gets the lyrics of a given song and checks if the result matches.
     * @param ctx: Context on which the game is running.
     * @param songName: Name of the song being played.
     * @param artistName: Name of the artist of the song being played.
     * @param speechInput: The inputted string from the speech.
     * @param gameManager: The gameManager instance that is managing the game.
     */
    private fun getAndCheckLyrics(ctx: Context, songName: String, artistName: String, speechInput: String, gameManager: GameManager) {
        //val service = LyricsOVHApiInterface.createLyricService()
        val service = createLyricService(LyricAPI())
        val call = service.getLyrics(artistName, songName)

        call.enqueue(object : Callback<Lyric> {
            override fun onFailure(call: Call<Lyric>?, t: Throwable?) {}

            override fun onResponse(call: Call<Lyric>?, response: Response<Lyric>?) {

                if (response != null) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        lyrics = result.lyrics.toString().replace(",", "")
                        checkAnswer(ctx, speechInput, lyrics, gameManager) // be sure the lyrics is ready when checking
                    } else {
                        toastShowSimpleMsg(ctx, R.string.no_lyrics_found)
                        endRound(gameManager)
                    }
                }
            }
        })
    }

    /**
     * Shows the result of lyrics matching
     * @param ctx: Context on which the game is running.
     * @param lyricToBeCheck: The string clause that has to be compared to the actual lyrics.
     * @param lyrics: Actual lyrics.
     * @param gameManager: The gameManager instance that is managing the game.
     */
    private fun checkAnswer(ctx: Context, lyricToBeCheck: String, lyrics: String, gameManager: GameManager) {
         if (lyrics.contains(lyricToBeCheck, ignoreCase = true)) {
             gameManager.increaseScore()
             gameManager.addCorrectSong()
             hasWon(ctx, gameManager.getScore(), true)
         } else {
             gameManager.addWrongSong()
             hasWon(ctx, gameManager.getScore(), false)
         }
        endRound(gameManager)
    }

    override fun onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
        super.onDestroy()
    }

    /**
     * Announces if the player has won or not
     * @param ctx: Context on which the game is running.
     * @param score: The score of the player.
     * @param hasWon: Indicator of whether the player has won the game or lost.
     */
    private fun hasWon(ctx: Context, score: Int, hasWon: Boolean) {
        if (hasWon) {
            toastShowCorrect(ctx, score)
        } else {
            toastShowSimpleMsg(ctx, R.string.wrong_message)
        }
    }

    /**
     * Custom handle of the bar progress.
     * @param ctx: Context on which the game is running.
     * @param myBar: The progression bar/timer.
     */
    private fun barTimer(ctx: Context, myBar: ProgressBar) {
        initializeBarTimer(myBar)
        runnable = object : Runnable {
            override fun run() {
                if (myBar.progress > 0) {
                    decreaseBarTimer(myBar)
                    handler.postDelayed(this, 999)
                } else if (myBar.progress == 0) {
                    if (this@LyricsBelongGameActivity::gameManager.isInitialized) {
                        if (speechInput == "-1"){
                            gameManager.addWrongSong()
                            endRound(gameManager)
                        } else {
                            getAndCheckLyrics(ctx, songName, artistName, speechInput, gameManager)
                        }
                    }
                }
            }
        }
        handler.post(runnable!!)
    }

    /**
     * Function to set scores in the end of the game
     */
    private fun setScores() {
        if(::gameManager.isInitialized) {
            super.setScores(gameManager)
        }
    }

    /*
     * The following functions are helper for testing
     */
    fun testCheckLyrics(ctx: Context, lyricToBeCheck: String, lyrics: String, gameManager: GameManager) {
        checkAnswer(ctx, lyricToBeCheck, lyrics, gameManager)
    }

    fun testUpdateSpeechResult(speechInput: String) {
        updateSpeechResult(speechInput)
    }

    fun testGetAndCheckLyrics(ctx: Context, songName: String, artistName: String, speechInput: String, gameManager: GameManager) {
        getAndCheckLyrics(ctx, songName, artistName, speechInput, gameManager)
    }

    fun getSongName(): String {
        return songName
    }

    fun getArtistName(): String {
        return artistName
    }

    fun testProgressBar(progressTime:Int = 0) {
        superTestProgressBar(findViewById(R.id.progressBarLyrics), progressTime)
    }

    fun testStartRound(ctx: Context, gameManager: GameManager) {
        startRound(ctx, gameManager)
    }

}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.api.*
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Lyric
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
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
    private lateinit var speechInput: String
    private lateinit var lyrics: String
    private var songName = "Thunder"
    private var artistName = "Imagine Dragons"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_belong_game)

        val getIntent = intent.extras
        if (getIntent != null) {
            gameManager = getIntent.getSerializable("gameManager") as GameManager
            super.setMax(intent)
            setFirstSong(gameManager)
        }

        val btnSpeak = findViewById<ImageView>(R.id.btnSpeak)
        btnSpeak.setOnClickListener {
            getSpeechInput()
        }

        val btnCheck = findViewById<Button>(R.id.lyricMatchButton)
        btnCheck.visibility = View.INVISIBLE
        btnCheck.setOnClickListener {
            getAndCheckLyrics(songName, artistName, speechInput, gameManager)
        }

        val btnNext = findViewById<Button>(R.id.nextSongButton)
        btnNext.setOnClickListener {
            playRound(gameManager)
        }

        barTimer(findViewById(R.id.progressBarLyrics))
    }

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
     * and setting new song for the round.
     */
    private fun playRound(gameManager: GameManager) {
        if (gameManager.checkGameStatus() && gameManager.setNextSong()) {
            clearResult()
            findViewById<Button>(R.id.lyricMatchButton).visibility = View.INVISIBLE
            songName = gameManager.currentSong.getTrackName()
            artistName = gameManager.currentSong.getArtistName()
            findViewById<TextView>(R.id.lyricResult).text = "Say something from $songName - $artistName"
            checkRunnable()
            barTimer(findViewById(R.id.progressBarLyrics))
        } else {
            switchToEnding(gameManager)
        }
    }

    /**
     * display the given String in lyricResult
     */
    private fun updateSpeechResult(speechInput: String) {
        findViewById<TextView>(R.id.lyricResult).text = speechInput
        findViewById<Button>(R.id.lyricMatchButton).visibility = View.VISIBLE
    }

    /**
     * get the lyrics of a given song
     */
    private fun getAndCheckLyrics(songName: String, artistName: String, speechInput: String, gameManager: GameManager) {
        val service = LyricsOVHApiInterface.createLyricService()
        val call = service.getLyrics(artistName, songName)
        call.enqueue(object : Callback<Lyric> {
            override fun onFailure(call: Call<Lyric>?, t: Throwable?) {}

            override fun onResponse(call: Call<Lyric>?, response: Response<Lyric>?) {

                if (response != null) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        lyrics = result.lyrics.toString().replace(",", "")
                        checkLyrics( speechInput, lyrics, gameManager) // be sure the lyrics is ready when checking
                    } else {
                        findViewById<TextView>(R.id.lyricMatchResult).text =
                            "No lyrics found, try another song"
                    }
                }
            }
        })
    }

    /**
     * show the result of lyrics matching
     */
    private fun checkLyrics(lyricToBeCheck: String, lyrics: String, gameManager: GameManager) {
         if (lyrics.contains(lyricToBeCheck, ignoreCase = true)) {
             gameManager.increaseScore()
             gameManager.addCorrectSong()
             findViewById<TextView>(R.id.lyricMatchResult).text = "res: correct"
         } else {
             gameManager.addWrongSong()
             findViewById<TextView>(R.id.lyricMatchResult).text = "res: too bad"
         }
    }

    // FIXME: progress bar doesn't decrease
    private fun barTimer(myBar: ProgressBar) {
        initializeBarTimer(myBar)
        runnable = object : Runnable {
            override fun run() {
                if (myBar.progress > 0) {
                    decreaseBarTimer(myBar)
                    h.postDelayed(this, 999)
                } else if (myBar.progress == 0) {
                    getAndCheckLyrics(songName, artistName, speechInput, gameManager)
                }
            }
        }
    }

    private fun clearResult() {
        findViewById<TextView>(R.id.lyricMatchResult).text = "result will show here"
    }

    private fun setFirstSong(gameManager: GameManager) {
        gameManager.currentSong = Song.singleSong(
            ItunesMusicApi.querySong(songName + " " + artistName, OkHttpClient(), 1).get()
        )
    }

    // helper functions to test private functions
    fun testCheckLyrics(lyricToBeCheck: String, lyrics: String, gameManager: GameManager) {
        checkLyrics(lyricToBeCheck, lyrics, gameManager)
    }

    fun testUpdateSpeechResult(speechInput: String) {
        updateSpeechResult(speechInput)
    }

    fun testGetAndCheckLyrics(songName: String, artistName: String, speechInput: String, gameManager: GameManager) {
        getAndCheckLyrics(songName, artistName, speechInput, gameManager)
    }
}
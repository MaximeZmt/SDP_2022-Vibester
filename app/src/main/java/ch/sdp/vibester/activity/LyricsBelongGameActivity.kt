package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.api.LyricsOVHApiInterface
import ch.sdp.vibester.api.ServiceBuilder
import ch.sdp.vibester.model.Lyric
import ch.sdp.vibester.model.SongList
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private const val REQUEST_AUDIO = 100
private const val LASTFM_METHOD = "tag.gettoptracks"

/**
 * Game checks if the player say the lyrics of the given song correct
 */
class LyricsBelongGameActivity : AppCompatActivity() {
    private lateinit var speechInput : String
    private val baseUrlLyrics = "https://api.lyrics.ovh/"
    private val baseUrlLastFM = "https://ws.audioscrobbler.com/2.0/"
    private lateinit var lyrics : String
    private var songName = "Thunder"
    private var artistName = "Imagine Dragons"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_belong_game)

        val btnSpeak = findViewById<ImageView>(R.id.btnSpeak)
        btnSpeak.setOnClickListener {
            getSpeechInput()
        }

        val btnCheck = findViewById<Button>(R.id.lyricMatchButton)
        btnCheck.visibility = View.INVISIBLE
        btnCheck.setOnClickListener {
            getAndCheckLyrics(songName, artistName, speechInput)
        }

        val btnNext = findViewById<Button>(R.id.nextSongButton)
        btnNext.setOnClickListener {
            fetchSong()
        }
    }

    private fun getSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, REQUEST_AUDIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_AUDIO || data != null) {
            val res = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            speechInput = res?.get(0) ?: "Didn't catch"
            updateSpeechResult(speechInput)
        }
    }

    /**
     * fetch a song randomly for the game
     */
    private fun fetchSong() {
        val service = ServiceBuilder.buildService(baseUrlLastFM, LastfmApiInterface::class.java)
        val uri = LastfmUri(method = LASTFM_METHOD, tag = "english")
        val call = service.getSongList(uri.convertToHashmap())
        call.enqueue(object: Callback<Any> {
            override fun onFailure(call: Call<Any>?, t: Throwable?) {}
            override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                if (response != null) {
                    val track = SongList(Gson().toJson(response.body()), uri.method).getShuffledSongList()[0]
                    songName = track.first
                    artistName = track.second
                    findViewById<TextView>(R.id.lyricResult).text = "Say something from %s - %s".format(songName, artistName)
                }
            }
        })

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
    private fun getAndCheckLyrics(songName: String, artistName: String, speechInput: String) {
        val service = ServiceBuilder.buildService(baseUrlLyrics,LyricsOVHApiInterface::class.java)
        val call = service.getLyrics(artistName, songName)
        call.enqueue(object: Callback<Lyric> {
            override fun onFailure(call: Call<Lyric>?, t: Throwable?) {}

            override fun onResponse(call: Call<Lyric>?, response: Response<Lyric>?) {

                if (response != null) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        lyrics = result.lyrics.toString().replace(",", "")
                        checkLyrics(speechInput, lyrics) // be sure the lyrics is ready when checking
                    } else {
                        findViewById<TextView>(R.id.lyricMatchResult).text = "No lyrics found, try another song"
                    }
                }
            }
        })
    }

    /**
     * show the result of lyrics matching
     */
    private fun checkLyrics(lyricToBeCheck: String, lyrics: String) {
        findViewById<TextView>(R.id.lyricMatchResult).text = if (lyrics.contains(lyricToBeCheck, ignoreCase = true)) "res: correct" else "res: too bad"
    }

    // helper functions to test private functions
    fun testCheckLyrics(lyricToBeCheck: String, lyrics: String) {
        checkLyrics(lyricToBeCheck, lyrics)
    }

    fun testUpdateSpeechResult(speechInput: String) {
        updateSpeechResult(speechInput)
    }

    fun testGetAndCheckLyrics(songName: String, artistName: String, speechInput: String) {
        getAndCheckLyrics(songName, artistName, speechInput)
    }

    fun testFetchSong() {
        fetchSong()
    }
}
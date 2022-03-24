package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LyricsOVHApiInterface
import ch.sdp.vibester.model.Lyric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private const val REQUEST_AUDIO = 100

class LyricsBelongGameActivity : AppCompatActivity() {
    private lateinit var speechInput : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_belong_game)

        val btnSpeak = findViewById<ImageView>(R.id.btnSpeak)
        var btnSpeakIsClicked = false
        btnSpeak.setOnClickListener {
            btnSpeakIsClicked = true
            getSpeechInput()
        }

        val btnCheck = findViewById<Button>(R.id.lyricMatchButton)
        btnCheck.setOnClickListener {
            if (btnSpeakIsClicked) {
                checkLyrics(speechInput)
            }
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
            findViewById<TextView>(R.id.lyricResult).text = speechInput
        }
    }

    private fun checkLyrics(lyricToBeCheck: String) {
        val service = LyricsOVHApiInterface.create()
        val call = service.getLyrics("Imagine Dragons", "Thunder")
        call.enqueue(object: Callback<Lyric> {
            override fun onFailure(call: Call<Lyric>?, t: Throwable?) {}

            override fun onResponse(call: Call<Lyric>?, response: Response<Lyric>?) {
                if (response != null) {
                    val originLyric = response.body().lyrics.toString()
                    findViewById<TextView>(R.id.lyricMatchResult).text = if (originLyric.contains(lyricToBeCheck, ignoreCase = true)) "correct" else "too bad"
                }
            }
        })
    }

    // used to test the private checkLyrics function
    fun testCheckLyrics(lyricToBeCheck: String) {
        checkLyrics(lyricToBeCheck)
    }
}
package ch.sdp.vibester.games

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

class LyricsBelongGame : AppCompatActivity() {
    private lateinit var btnSpeak : ImageView
    private lateinit var txtFromSpeech: TextView
    private lateinit var btnCheck : Button
    private lateinit var txtResult : TextView
    private lateinit var originLyric : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_belong_game)

        btnSpeak = findViewById(R.id.btnSpeak)
        var btnSpeakIsClicked = false
        btnSpeak.setOnClickListener {
            btnSpeakIsClicked = true
            getSpeechInput()
        }

        txtFromSpeech = findViewById(R.id.lyricResult)

        btnCheck = findViewById(R.id.lyricMatchButton)
        btnCheck.setOnClickListener {
            if (btnSpeakIsClicked) {
                getLyrics()
            }
        }

        txtResult = findViewById(R.id.lyricMatchResult)
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
            txtFromSpeech.text = res?.get(0) ?: " Didn't catch"
        }
    }

    private fun getLyrics() {
        val service = LyricsOVHApiInterface.create()
        val call = service.getLyrics("Imagine Dragons", "Thunder")
        call.enqueue(object: Callback<Lyric> {
            override fun onFailure(call: Call<Lyric>?, t: Throwable?) {}

            override fun onResponse(call: Call<Lyric>?, response: Response<Lyric>?) {
                if (response != null) {
                    originLyric = response.body().lyrics.toString()
                    checkCorrectness()
                }
            }
        })
    }

    private fun checkCorrectness() {
        if (originLyric.contains(txtFromSpeech.text, ignoreCase = true)) {
            txtResult.text = "correct!"
        } else {
            txtResult.text = "too bad"
        }
    }
}
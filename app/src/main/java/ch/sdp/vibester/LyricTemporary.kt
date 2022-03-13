package ch.sdp.vibester

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.api.MusixmatchApi
import ch.sdp.vibester.model.Lyric
import okhttp3.OkHttpClient


class LyricTemporary: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric_temporary)

        val artistName = findViewById<EditText>(R.id.artistForLyric)

        val trackName = findViewById<EditText>(R.id.trackForLyric)

        val btnValidate = findViewById<Button>(R.id.validateForLyric)

        val textViewLyric = findViewById<TextView>(R.id.lyricBody)

        btnValidate.setOnClickListener {
            val lyric = Lyric(MusixmatchApi.queryLyric(
                artistName.text.toString(), trackName.text.toString(),
                OkHttpClient()).get())

            textViewLyric.text = lyric.getLyricBody()
            textViewLyric.movementMethod = ScrollingMovementMethod()
        }
    }
}
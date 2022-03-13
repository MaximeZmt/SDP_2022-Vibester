package ch.sdp.vibester

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.api.LyricsOVHApiInterface
import ch.sdp.vibester.model.Lyric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LyricTemporary: AppCompatActivity() {
    val baseUrl = "https://api.lyrics.ovh/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric_temporary)

        val artistName = findViewById<EditText>(R.id.artistForLyric)

        val trackName = findViewById<EditText>(R.id.trackForLyric)

        val btnValidate = findViewById<Button>(R.id.validateForLyric)

        val textViewLyric = findViewById<TextView>(R.id.lyricBody)
        textViewLyric.movementMethod = ScrollingMovementMethod()

        btnValidate.setOnClickListener {
            var retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(LyricsOVHApiInterface::class.java)
            val call = service.getLyrics(artistName.text.toString(), trackName.text.toString())
            call.enqueue(object: Callback<Lyric>{
                override fun onFailure(call: Call<Lyric>?, t: Throwable?) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<Lyric>?, response: Response<Lyric>?) {
                    if (response != null) {
                        textViewLyric.text = response.body().lyrics
                    }
                }
            })
        }
    }
}
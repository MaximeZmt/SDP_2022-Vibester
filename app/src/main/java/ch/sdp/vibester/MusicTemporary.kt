package ch.sdp.vibester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.sdp.vibester.api.AudioPlayer
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient

class MusicTemporary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_temporary)

        val txtInput = findViewById<EditText>(R.id.musicName)

        val btnValidate = findViewById<Button>(R.id.validate)

        val textViewPlaying = findViewById<TextView>(R.id.textViewPlaying)

        btnValidate.setOnClickListener {
            val song = Song.singleSong(ItunesMusicApi.querySong(txtInput.text.toString(), OkHttpClient(), 1).get())
            AudioPlayer.playAudio(song.getPreviewUrl())
            textViewPlaying.setText(song.getArtistName() + " - " + song.getTrackName())
        }


    }
}
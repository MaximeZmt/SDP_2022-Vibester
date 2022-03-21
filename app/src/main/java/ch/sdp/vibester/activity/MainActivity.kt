package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.GenreTemporary
import ch.sdp.vibester.LyricTemporary
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.IntentSwitcher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnWelcome = findViewById<Button>(R.id.placeholder_welcome)
        btnWelcome.setOnClickListener {
            IntentSwitcher.switchBackToWelcome(this)
        }

        val btnLyric = findViewById<Button>(R.id.lyricButton)
        val lyricIntent = Intent(this, LyricTemporary::class.java)
        btnLyric.setOnClickListener {
            startActivity(lyricIntent)
        }

        val btnGenre = findViewById<Button>(R.id.genreButton)
        val genreIntent = Intent(this, GenreTemporary::class.java)
        btnGenre.setOnClickListener {
            startActivity(genreIntent)

        }

    }

}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.GenreTemporary
import ch.sdp.vibester.LyricTemporary
import ch.sdp.vibester.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

    fun switchToWelcome(view: View) {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
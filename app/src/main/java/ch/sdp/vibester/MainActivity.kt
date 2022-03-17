package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.games.TypingGame
import ch.sdp.vibester.profile.ProfileDataProvider
import ch.sdp.vibester.scoreboard.ScoreBoardActivity
import ch.sdp.vibester.profile.ProfileSetup
import ch.sdp.vibester.profile.UserProfile
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtInput = findViewById<EditText>(R.id.mainNameInput)

        val btnGreeting = findViewById<Button>(R.id.mainButton)

        val greetingIntent = Intent(this, GreetingActivity::class.java)
            btnGreeting.setOnClickListener {
                greetingIntent.putExtra("name", txtInput.text.toString())
                startActivity(greetingIntent)
        }

        val btnLyric = findViewById<Button>(R.id.lyricButton)
        val lyricIntent = Intent(this, LyricTemporary::class.java)
        btnLyric.setOnClickListener {
            startActivity(lyricIntent)
        }

    }

    fun switchToWelcome(view: View) {
        val intent = Intent(this, WelcomeScreen::class.java)
        startActivity(intent)
    }
}
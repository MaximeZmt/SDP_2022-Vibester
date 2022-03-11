package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.scoreboard.ScoreBoardActivity

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

        // button to scoreboard
        // FIXME: scoreboard enter button need to be move to the welcome screen
        val btnScoreboard = findViewById<Button>(R.id.scoreboardButton)
        val scoreboardIntent = Intent(this, ScoreBoardActivity::class.java)

        btnScoreboard.setOnClickListener {
            startActivity(scoreboardIntent)
        }

        // button to local (buzzer) game
        val btnLocalGame = findViewById<Button>(R.id.toLocalGameButton)
        val gamescreenIntent = Intent(this, GamescreenActivity::class.java)

        btnLocalGame.setOnClickListener {
            startActivity(gamescreenIntent)
        }
    }

    fun switchToWelcome(view: View) {
        val intent = Intent(this, WelcomeScreen::class.java)
        startActivity(intent)
    }
}
package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
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
<<<<<<< HEAD

        val gamescreenIntent = Intent(this, GamescreenActivity::class.java)
=======
        val greetingIntent = Intent(this, GreetingActivity::class.java)
>>>>>>> 10f227f71c430aeb8ad28cad04051b08a126ae15

        btnGreeting.setOnClickListener {
            //gamescreenIntent.putExtra("name", txtInput.text.toString())
            startActivity(gamescreenIntent)
        }

<<<<<<< HEAD

=======
        // button to scoreboard
        // FIXME: scoreboard enter button need to be move to the welcome screen
        val btnScoreboard = findViewById<Button>(R.id.scoreboardButton)
        val scoreboardIntent = Intent(this, ScoreBoardActivity::class.java)

        btnScoreboard.setOnClickListener {
            startActivity(scoreboardIntent)
        }
>>>>>>> 10f227f71c430aeb8ad28cad04051b08a126ae15
    }
}
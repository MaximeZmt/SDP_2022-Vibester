package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.scoreboard.ScoreBoardActivity
import ch.sdp.vibester.profile.ProfileSetup

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



        val btnProfile = findViewById<Button>(R.id.profileButton)
        val profileIntent = Intent(this, ProfileSetup::class.java)

        btnProfile.setOnClickListener{
            val user = (0..5).random().toString()
            profileIntent.putExtra("userID", user)
            startActivity(profileIntent)
        }

        val btnScoreboard = findViewById<Button>(R.id.scoreboardButton)
        val scoreboardIntent = Intent(this, ScoreBoardActivity::class.java)

        btnScoreboard.setOnClickListener {
            startActivity(scoreboardIntent)
        }
    }

}
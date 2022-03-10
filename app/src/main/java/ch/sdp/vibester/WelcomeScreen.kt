package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.sdp.vibester.scoreboard.ScoreBoardActivity

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
    }

    fun switchToPlay(view: View) {
        val intent = Intent(this, GameSetupScreen::class.java)
        startActivity(intent)
    }

    fun switchToProfile(view: View) { //FILLER INTENT
        val intent = Intent(this, GameSetupScreen::class.java)
        startActivity(intent)
    }

    fun switchToScoreboard(view: View) {
        val intent = Intent(this, ScoreBoardActivity::class.java)
        startActivity(intent)
    }

    fun switchToListen(view: View) { //FILLER INTENT
        val intent = Intent(this, GameSetupScreen::class.java)
        startActivity(intent)
    }

    fun switchToSettings(view: View) { //FILLER INTENT
        val intent = Intent(this, GameSetupScreen::class.java)
        startActivity(intent)
    }

    /*fun switchToLogin(view: View) { //FILLER INTENT
        val intent = Intent(this, GameSetupScreen::class.java)
        startActivity(intent)
    }*/
}
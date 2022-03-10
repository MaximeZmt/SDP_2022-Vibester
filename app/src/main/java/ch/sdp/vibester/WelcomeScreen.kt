package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
    }

    fun switchToPlay(view: View) {
        val intent = Intent()
        startActivity(intent)
    }

    fun switchToProfile(view: View) {
        val intent = Intent()
        startActivity(intent)
    }

    fun switchToScoreboard(view: View) {
        val intent = Intent()
        startActivity(intent)
    }

    fun switchToListen(view: View) {
        val intent = Intent()
        startActivity(intent)
    }

    fun switchToSettings(view: View) {
        val intent = Intent()
        startActivity(intent)
    }

    fun switchToLogin(view: View) {
        val intent = Intent()
        startActivity(intent)
    }
}
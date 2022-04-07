package ch.sdp.vibester.activity

//import ch.sdp.vibester.profile.ProfileDataProvider
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.UserSharedPref

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_welcome_screen)

        val tv = findViewById<TextView>(R.id.user_status)
        val username = UserSharedPref.getUser(this).username
        if(username != "")
        {
            tv.text = "User: " + username
        }

        val currentEmail = UserSharedPref.getUser(this).email
        if (currentEmail != null){
            UserSharedPref.userReset(this, currentEmail)
        }
    }

    private fun sendDirectIntent(arg: Class<*>?) {
        val intent = Intent(this, arg)
        startActivity(intent)
    }

    fun switchToPlay(view: View) {
        sendDirectIntent(GameSetupActivity::class.java)
    }

    fun switchToProfile(view: View) {
        sendDirectIntent(ProfileActivity::class.java)
    }

    fun switchToScoreboard(view: View) {
        sendDirectIntent(ScoreBoardActivity::class.java)
    }

    fun switchToSettings(view: View) {
        sendDirectIntent(AuthenticationActivity::class.java)
    }

    /*
     * Belongs to a previously implemented button, taken out for UI purposes.
     * Might bring it back, thus leaving the code for now.
     */

    /*fun switchToLogin(view: View) { //FILLER INTENT
        sendDirectIntent(GameSetupScreen::class.java)
    }*/
}
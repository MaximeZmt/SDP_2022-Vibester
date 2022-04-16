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
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.UserSharedPref
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_welcome_screen)

        //UserSharedPref.userReset(applicationContext, "mickey@mouse.com")
        Log.e("MAILLL ", UserSharedPref.getUser(applicationContext).email)
        Log.e("IMAGE ", UserSharedPref.getUser(applicationContext).image)
        Log.e("CURR FIREBASE USER: ", FirebaseAuth.getInstance().currentUser?.email.toString())



        val tv = findViewById<TextView>(R.id.user_status)
        val username = UserSharedPref.getUser(applicationContext).username
        Log.e("USN: ", username)
        if(FirebaseAuth.getInstance().currentUser != null)
        {
            tv.text = "User: " + FirebaseAuth.getInstance().currentUser?.email.toString()
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

    fun switchToDownload(view: View) {
        sendDirectIntent(DownloadActivity::class.java)
    }

    /*
     * Belongs to a previously implemented button, taken out for UI purposes.
     * Might bring it back, thus leaving the code for now.
     */

    /*fun switchToLogin(view: View) { //FILLER INTENT
        sendDirectIntent(GameSetupScreen::class.java)
    }*/
}
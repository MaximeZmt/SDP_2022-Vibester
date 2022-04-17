package ch.sdp.vibester.activity

//import ch.sdp.vibester.profile.ProfileDataProvider
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ch.sdp.vibester.model.UserSharedPref

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_welcome_screen)



        val userStatusTextValue = findViewById<TextView>(R.id.user_status)
        if(FireBaseAuthenticator.isLoggedIn())
        {
            userStatusTextValue.text = "User: " + FireBaseAuthenticator.getCurrentUserMail()
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
        if (FireBaseAuthenticator.isLoggedIn()){
            sendDirectIntent(ProfileActivity::class.java)
        }else{
            sendDirectIntent(AuthenticationActivity::class.java)
        }
    }

    fun switchToScoreboard(view: View) {
        sendDirectIntent(ScoreBoardActivity::class.java)
    }

    fun switchToDownload(view: View) {
        sendDirectIntent(DownloadActivity::class.java)
    }

    fun switchToSearch(view: View) {
        sendDirectIntent(SearchUserActivity::class.java)
    }
}
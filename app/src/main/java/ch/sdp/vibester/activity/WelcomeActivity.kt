package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.api.InternetState
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.database.PersistanceSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_welcome_screen)

        updateUserConnectionStatus()

        PersistanceSetter.setPersistance()
        Database.get()
    }

    override fun onResume() {
        super.onResume()
        updateUserConnectionStatus()
    }

    private fun updateUserConnectionStatus() {
        val userStatusTextValue = findViewById<TextView>(R.id.user_status)
        if(authenticator.isLoggedIn())
        {
            userStatusTextValue.text = "User: " + authenticator.getCurrUserMail()
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
        if (authenticator.isLoggedIn()){
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
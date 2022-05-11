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
import ch.sdp.vibester.helper.IntentSwitcher
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

    fun switchToPlay(view: View) {
        IntentSwitcher.switch(this, GameSetupActivity::class.java)
    }

    fun switchToProfile(view: View) {
        if (authenticator.isLoggedIn()){
            IntentSwitcher.switch(this, ProfileActivity::class.java)
        }else{
            IntentSwitcher.switch(this, AuthenticationActivity::class.java)
        }
    }

    fun switchToScoreboard(view: View) {
        IntentSwitcher.switch(this, ScoreBoardActivity::class.java)
    }

    fun switchToDownload(view: View) {
        IntentSwitcher.switch(this, DownloadActivity::class.java)
    }

    fun switchToSearch(view: View) {
        IntentSwitcher.switch(this, SearchUserActivity::class.java)
    }
}
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
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.database.PersistanceSetter


class WelcomeActivity : AppCompatActivity() {

    // For test purpose
    companion object{
        var testLoggedIn: Boolean = false

        fun setLoggedIn(){
            testLoggedIn = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_welcome_screen)

        val userStatusTextValue = findViewById<TextView>(R.id.user_status)
        if(FireBaseAuthenticator.isLoggedIn() ||  testLoggedIn)
        {
            userStatusTextValue.text = "User: " + FireBaseAuthenticator.getCurrentUserMail()
        }

        val startOfApp = !PersistanceSetter.getPersistance()

        Log.e("INFO: Should Access", ((InternetState.hasAccessedInternetOnce(this))).toString())
        PersistanceSetter.setPersistance()
        Database.get()
    }

    private fun sendDirectIntent(arg: Class<*>?) {
        val intent = Intent(this, arg)
        startActivity(intent)
    }

    fun switchToPlay(view: View) {
        sendDirectIntent(GameSetupActivity::class.java)
    }

    fun switchToProfile(view: View) {
        if (FireBaseAuthenticator.isLoggedIn() || testLoggedIn){
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
        // Disable User Search if not connected
        if(!FireBaseAuthenticator.isLoggedIn() && !TestMode.isTest()){
            val buttonToSearchUser = findViewById<Button>(R.id.welcome_search)
            buttonToSearchUser.isEnabled = false
        }else{
            sendDirectIntent(SearchUserActivity::class.java)
        }
    }
}
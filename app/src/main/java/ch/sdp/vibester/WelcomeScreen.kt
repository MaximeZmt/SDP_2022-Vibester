package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.sdp.vibester.profile.ProfileDataProvider
import ch.sdp.vibester.profile.ProfileSetup
import ch.sdp.vibester.profile.UserProfile
import ch.sdp.vibester.scoreboard.ScoreBoardActivity

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
    }

    private fun sendDirectIntent(arg: Class<*>?) {
        val intent = Intent(this, arg)
        startActivity(intent)
    }

    fun switchToPlay(view: View) {
        sendDirectIntent(GameSetupScreen::class.java)
    }

    fun switchToProfile(view: View) { //FILLER INTENT
//        sendDirectIntent(GameSetupScreen::class.java)
        val profileIntent = Intent(this, ProfileSetup::class.java)
        val userID = (0..5).random().toString()
        val dataProvider = userID.let { ProfileDataProvider(it) }
        val user: UserProfile = dataProvider.getUserProfileData()
        profileIntent.putExtra("userProfile",  user)
        startActivity(profileIntent)
    }

    fun switchToScoreboard(view: View) {
        sendDirectIntent(ScoreBoardActivity::class.java)
    }

    fun switchToListen(view: View) { //FILLER INTENT
        sendDirectIntent(MusicTemporary::class.java)
    }

    fun switchToSettings(view: View) { //FILLER INTENT
        sendDirectIntent(GameSetupScreen::class.java)
    }

    /*
     * Belongs to a previously implemented button, taken out for UI purposes.
     * Might bring it back, thus leaving the code for now.
     */

    /*fun switchToLogin(view: View) { //FILLER INTENT
        sendDirectIntent(GameSetupScreen::class.java)
    }*/
}
package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.*
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.database.PersistanceSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
* WelcomeScreen Fragment with a button in the bottom navigation.
*/
@AndroidEntryPoint
class WelcomeScreenFragment : Fragment(),View.OnClickListener {

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

        val view = inflater.inflate(R.layout.fragment_welcome_screen, container, false)

        view.findViewById<Button>(R.id.welcome_profile).setOnClickListener(this)
        view.findViewById<Button>(R.id.welcome_download).setOnClickListener(this)
        view.findViewById<Button>(R.id.welcome_scoreboard).setOnClickListener(this)
        view.findViewById<Button>(R.id.welcome_search).setOnClickListener(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUserConnectionStatus(view)

        PersistanceSetter.setPersistance()
        Database.get()
    }

    private fun updateUserConnectionStatus(view: View) {
        val userStatusTextValue = view.findViewById<TextView>(R.id.user_status)
        if(authenticator.isLoggedIn())
        {
            userStatusTextValue.text = "User: " + authenticator.getCurrUserMail()
        }
    }

    private fun sendDirectIntent(arg: Class<*>?) {
        val intent = Intent(requireContext(), arg)
        startActivity(intent)
    }


    private fun switchToProfile() {
        if (authenticator.isLoggedIn()){
            sendDirectIntent(ProfileActivity::class.java)
        } else {
            sendDirectIntent(AuthenticationActivity::class.java)
        }
    }

    private fun switchToScoreboard() {
        sendDirectIntent(ScoreBoardActivity::class.java)
    }

    private fun switchToDownload() {
        sendDirectIntent(DownloadActivity::class.java)
    }

    private fun switchToSearch() {
        sendDirectIntent(SearchUserActivity::class.java)
    }

    override fun onClick(v: View?) {
        when(v!!.getId()) {
            R.id.welcome_search -> switchToSearch()
            R.id.welcome_download -> switchToDownload()
            R.id.welcome_profile -> switchToProfile()
            R.id.welcome_scoreboard -> switchToScoreboard()
        }
    }
}
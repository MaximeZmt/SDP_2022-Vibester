package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.database.PersistanceSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        updateUserConnectionStatus()

        PersistanceSetter.setPersistance()
        Database.get()
    }

    private fun updateUserConnectionStatus() {
        val userStatusTextValue = requireView().findViewById<TextView>(R.id.user_status)
        if(FireBaseAuthenticator.isLoggedIn())
        {
            userStatusTextValue.text = "User: " + authenticator.getCurrentUserMail()
        }
    }

    private fun sendDirectIntent(arg: Class<*>?) {
        val intent = Intent(requireContext(), arg)
        startActivity(intent)
    }

    fun switchToProfile() {
        if (FireBaseAuthenticator.isLoggedIn()){
            sendDirectIntent(ProfileActivity::class.java)
        }else{
            sendDirectIntent(AuthenticationActivity::class.java)
        }
    }

    fun switchToScoreboard() {
        sendDirectIntent(ScoreBoardActivity::class.java)
    }

    fun switchToDownload() {
        sendDirectIntent(DownloadActivity::class.java)
    }

    fun switchToSearch() {
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
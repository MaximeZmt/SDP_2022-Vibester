package ch.sdp.vibester.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import ch.sdp.vibester.R
const val EXTRA_ID = "userProfile"

class ProfileSetup: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bundle = intent.extras;
        val user: UserProfile = bundle?.getSerializable(EXTRA_ID) as UserProfile
        setupProfile(user)
    }

     fun setupProfile(user: UserProfile){
        findViewById<TextView>(R.id.handle).text =  user.handle
        findViewById<TextView>(R.id.username).text = user.username
        findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
        findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
        findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
        findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        /* TODO: add functionality to display the image
        findViewById<ImageView>(R.id.avatar).loadImg(user.image)*/
    }
}


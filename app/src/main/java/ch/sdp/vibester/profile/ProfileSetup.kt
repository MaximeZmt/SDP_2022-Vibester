package ch.sdp.vibester.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ch.sdp.vibester.R


class ProfileSetup: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    private fun setupProfile(user: UserProfile){
        findViewById<TextView>(R.id.handle).text =  user.handle
        findViewById<TextView>(R.id.username).text = user.username
        findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
        findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
        findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
        findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        Glide.with(this)
            .load(image)
            .circleCrop()
            .into(binding.imageView)
    }


}
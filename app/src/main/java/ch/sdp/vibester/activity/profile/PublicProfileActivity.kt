package ch.sdp.vibester.activity.profile

import android.os.Bundle
import ch.sdp.vibester.R

/** profile page of a user with only public information */
class PublicProfileActivity : ProfileActivity() {
    lateinit var userId: String

//    override fun onCreate(savedInstanceState: Bundle?) {
//        userId = intent.getStringExtra("UserId").toString()
//        super.onCreate(savedInstanceState)
//        if (intent.getStringExtra("ScoresOrFollowing") == R.string.profile_scores.toString()) {
//            showAHideB(R.id.profile_scroll_stat, R.id.profile_scroll_following)
//        } else if (intent.getStringExtra("ScoresOrFollowing") == R.string.profile_following.toString()) {
//            showAHideB(R.id.profile_scroll_following, R.id.profile_scroll_stat)
//        }
//    }

    override fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }
}
package ch.sdp.vibester.activity.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.sdp.vibester.R

/** profile page of a user with only public information */
class PublicProfileActivity : ProfileActivity() {
    lateinit var userId: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            userId = bundle.getString("UserId", null)
            queryDatabase()
            if (bundle.getString("ScoresOrFollowing") == R.string.profile_scores.toString()) {
                showAHideB(R.id.profile_scroll_stat, R.id.profile_scroll_following)
            } else if (bundle.getString("ScoresOrFollowing") == R.string.profile_following.toString()) {
                showAHideB(R.id.profile_scroll_following, R.id.profile_scroll_stat)
            }
            setupRecycleViewForFriends()

            setFollowingScoresBtnListener(R.id.profile_scores, R.id.profile_scroll_stat, R.id.profile_scroll_following)
            setFollowingScoresBtnListener(R.id.profile_following, R.id.profile_scroll_following, R.id.profile_scroll_stat)
        }
    }

    override fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }
}
package ch.sdp.vibester.activity

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TableLayout
import androidx.core.widget.NestedScrollView
import ch.sdp.vibester.R

/** profile page of a user with only public information */
class PublicProfileActivity : ProfileActivity() {
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        userId = intent.getStringExtra("UserId").toString()
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("ScoresOrFriends") == "Scores") {
            showAHideB(R.id.profile_scroll_stat, R.id.profile_scroll_friends)
        } else if (intent.getStringExtra("ScoresOrFriends") == "Friends") {
            showAHideB(R.id.profile_scroll_friends, R.id.profile_scroll_stat)
        }
    }


    override fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }
}
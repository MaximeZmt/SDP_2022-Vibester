package ch.sdp.vibester.activity.profile

import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.TableLayout
import ch.sdp.vibester.R

/** profile page of a user with only public information */
class PublicProfileActivity : ProfileActivity() {
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        userId = intent.getStringExtra("UserId").toString()
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("ScoresOrFriends") == "Scores") {
            findViewById<TableLayout>(R.id.profileStatistics).visibility = VISIBLE
        }
    }

    override fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }
}
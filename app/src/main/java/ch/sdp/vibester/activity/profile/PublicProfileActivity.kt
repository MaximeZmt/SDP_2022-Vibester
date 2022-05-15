package ch.sdp.vibester.activity.profile

import android.os.Bundle

/** profile page of a user with only public information */
class PublicProfileActivity : ProfileActivity() {
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        userId = intent.getStringExtra("UserId").toString()
        super.onCreate(savedInstanceState)
    }

    override fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }
}
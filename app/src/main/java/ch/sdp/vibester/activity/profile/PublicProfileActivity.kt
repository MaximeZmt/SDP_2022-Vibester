package ch.sdp.vibester.activity.profile

/** profile page of a user with only public information */
class PublicProfileActivity : ProfileActivity() {

    override fun queryDatabase() {
        val userId = intent.getStringExtra("UserId")
        if (userId != null) {
            dataGetter.getUserData(userId, this::setupProfile)
        }
    }
}
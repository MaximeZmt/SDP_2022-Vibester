package ch.sdp.vibester.activity.profile


class PublicProfileActivity : ProfileActivity() {

    override fun queryDatabase() {
        val userId = intent.getStringExtra("UserId")
        if (userId != null) {
            dataGetter.getUserData(userId, this::setupProfile)
        }
    }
}
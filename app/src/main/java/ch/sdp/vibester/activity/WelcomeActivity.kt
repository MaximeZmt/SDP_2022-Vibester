package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.model.Song
//import ch.sdp.vibester.profile.ProfileDataProvider
import ch.sdp.vibester.profile.UserProfile

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_welcome_screen)
    }

    private fun sendDirectIntent(arg: Class<*>?) {
        val intent = Intent(this, arg)
        startActivity(intent)
    }

    fun switchToPlay(view: View) {
        sendDirectIntent(GameSetupActivity::class.java)
    }

    fun switchToProfile(view: View) { //FILLER INTENT
////        sendDirectIntent(GameSetupScreen::class.java)
//        val profileIntent = Intent(this, ProfileActivity::class.java)
//        val userID = (0..5).random().toString()
//        val dataProvider = userID.let { ProfileDataProvider(it) }
//        val user: UserProfile = dataProvider.getUserProfileData()
//        profileIntent.putExtra("userProfile",  user)
        sendDirectIntent(ProfileActivity::class.java)
    }

    fun switchToScoreboard(view: View) {
        sendDirectIntent(ScoreBoardActivity::class.java)
    }

    fun switchToListen(view: View) { //FILLER INTENT

        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val mySong = Song.singleSong(inputTxt)

        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("song", mySong)
        newIntent.putExtra("isPlaying", true)
        newIntent.putExtra("hasWon", true)
        startActivity(newIntent)
    }

    fun switchToSettings(view: View) { //FILLER INTENT
        sendDirectIntent(AuthenticationActivity::class.java)
    }

    /*
     * Belongs to a previously implemented button, taken out for UI purposes.
     * Might bring it back, thus leaving the code for now.
     */

    /*fun switchToLogin(view: View) { //FILLER INTENT
        sendDirectIntent(GameSetupScreen::class.java)
    }*/
}
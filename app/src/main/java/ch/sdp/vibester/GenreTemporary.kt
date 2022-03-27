package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.api.LastfmApi
//import ch.sdp.vibester.api.LastfmHelper
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.helper.GameManager
import okhttp3.OkHttpClient

/**
 * Activity to show the list of songs for a chosen tag
 */
class GenreTemporary : AppCompatActivity() {
    private val BY_TAG = "tag.gettoptracks"
    private val BY_CHART = "chart.gettoptracks"
    private val BY_ARTIST = "artist.gettoptracks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
    }

    /**
     * Fetch data from Lastfm and show song list in a ListView
     * @param method: BY_TAG or BY_CHART (top tracks without tag)
     * @param tag: tag name if method BY_TAG is chosen
     */
    fun performQuery(uri:LastfmUri){
        val gameManager = GameManager()
//        val songs = LastfmApi.querySongList(OkHttpClient(),uri).get()
        gameManager.setGameSongList(LastfmApi.querySongList(OkHttpClient(),uri).get(),uri.method)
        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        startActivity(newIntent)
    }

    fun playRock(view: View) {
        performQuery(LastfmUri(method = BY_TAG, tag = "rock"))
    }

    fun playImagineDragons(view: View) {
        performQuery(LastfmUri(method = BY_ARTIST, artist = "Imagine Dragons"))
    }

    fun playTopTracks(view: View) {
        performQuery(LastfmUri(method = BY_CHART))
    }

    fun playBTS(view: View) {
        performQuery(LastfmUri(method = BY_ARTIST, artist = "BTS"))
    }

    fun playKpop(view: View) {
        performQuery(LastfmUri(method = BY_TAG, tag = "kpop"))
    }

    fun playBillieEilish(view: View){
        performQuery(LastfmUri(method = BY_ARTIST, artist = "Billie Eilish"))
    }

}
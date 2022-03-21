package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import ch.sdp.vibester.api.LastfmHelper
import ch.sdp.vibester.games.GameManager
import ch.sdp.vibester.games.TypingGame
import ch.sdp.vibester.model.Song

/**
 * Activity to show the list of songs for a chosen tag
 */
class GenreTemporary : AppCompatActivity() {
    private val BY_TAG = "tag.gettoptracks"
    private val BY_CHART = "chart.gettoptracks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
    }

    /**
     * Fetch data from Lastfm and show song list in a ListView
     * @param method: BY_TAG or BY_CHART (top tracks without tag)
     * @param tag: tag name if method BY_TAG is chosen
     */
    fun performQuery(method: String, tag: String=""){
        val gameManager = GameManager(method, tag)

        val newIntent = Intent(this, TypingGame::class.java)
        newIntent.putExtra("song", gameManager.nextSong())
        newIntent.putExtra("isPlaying", true)
        newIntent.putExtra("hasWon", true)
        newIntent.putExtra("gameManager", gameManager)
        startActivity(newIntent)
    }

    fun getKpopSongList(view: View) {
        performQuery(BY_TAG,"kpop")
    }

    fun getRockSongList(view: View) {
        performQuery(BY_TAG, "rock")
    }

    fun getTopSongList(view: View) {
        performQuery(BY_CHART)
    }

}
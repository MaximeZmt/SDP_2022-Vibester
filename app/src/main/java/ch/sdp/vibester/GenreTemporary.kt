package ch.sdp.vibester

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.api.LastfmHelper

/**
 * Testing main branch
 */
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
        val listSongs = findViewById<ListView>(R.id.songsListView)
        val songList = LastfmHelper.getRandomSongList(method, tag)
        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songList)
        listSongs.adapter = arr
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
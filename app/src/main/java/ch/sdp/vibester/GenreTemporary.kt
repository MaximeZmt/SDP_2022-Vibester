package ch.sdp.vibester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import ch.sdp.vibester.api.LastfmApi
import ch.sdp.vibester.api.LastfmHelper
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient

class GenreTemporary : AppCompatActivity() {
    private val BY_TAG = "tag.gettoptracks"
    private val BY_CHART = "chart.gettoptracks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
    }

    fun performQuery(method: String, tag: String=""){
        val listSongs = findViewById<ListView>(R.id.listSongs)
        val songList = LastfmHelper.getBothtSongList(method, tag)
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
package ch.sdp.vibester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import ch.sdp.vibester.api.LastfmApi
import ch.sdp.vibester.model.SongsList
import okhttp3.OkHttpClient

class GenreTemporary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
        val btnRock = findViewById<Button>(R.id.rock)
        val btnTop = findViewById<Button>(R.id.top)
        val btnKpop = findViewById<Button>(R.id.kpop)

        var listSongs = findViewById<ListView>(R.id.listSongs)

        btnRock.setOnClickListener {

            val songs_query1 = SongsList(LastfmApi.querySongsByTag(OkHttpClient(),"rock").get())
            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songs_query1.getSongs())
            listSongs.adapter = arr
        }

        btnTop.setOnClickListener {
            val songs_query1 = SongsList(LastfmApi.querySongsByChart(OkHttpClient()).get())
            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songs_query1.getSongs())
            listSongs.adapter = arr
        }

        btnKpop.setOnClickListener {
            val songs_query1 = SongsList(LastfmApi.querySongsByTag(OkHttpClient(),"kpop" ).get())
            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songs_query1.getSongs())
            listSongs.adapter = arr
        }
    }

}
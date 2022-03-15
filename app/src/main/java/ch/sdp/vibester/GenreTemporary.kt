package ch.sdp.vibester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import ch.sdp.vibester.api.LastfmApi
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.model.SongsList
import okhttp3.OkHttpClient

class GenreTemporary : AppCompatActivity() {
    private val BY_TAG = "tag.gettoptracks"
    private val BY_CHART = "chart.gettoptracks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
        val btnRock = findViewById<Button>(R.id.rock)
        val btnTop = findViewById<Button>(R.id.top)
        val btnKpop = findViewById<Button>(R.id.kpop)

        var listSongs = findViewById<ListView>(R.id.listSongs)

        btnRock.setOnClickListener {
//            val songs_rock = SongsList(LastfmApi.querySongsList(OkHttpClient(), LastfmUri(method = BY_TAG, tag = "rock")).get())
////            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songs_rock.getSongs())
//            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songs_rock.getSongs())

//            listSongs.adapter = arr
        }

    }

}
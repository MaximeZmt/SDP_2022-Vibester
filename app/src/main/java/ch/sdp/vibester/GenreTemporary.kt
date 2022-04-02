package ch.sdp.vibester

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity to show the list of songs for a chosen tag
 */
open class GenreTemporary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
    }

    /**
     * Fetch data from Lastfm and show song list in a ListView
     */
    open fun performQuery(uri: LastfmUri) {

        val service = LastfmApiInterface.createLastfmService()
        val call = service.getSongList(uri.convertToHashmap())
        call.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable?) {}

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                switchToGame(Gson().toJson(response.body()), uri.method)
            }
        })
    }

    // will be override in each derived class for different games
    open fun switchToGame(response: String, method: String) {}

    // view is needed, remove it will result failure in tests
    open fun playRock(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_TAG.method, tag = "rock"))
    }

    open fun playImagineDragons(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_ARTIST.method, artist = "Imagine Dragons"))
    }

    open fun playTopTracks(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_CHART.method))
    }

    open fun playBTS(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_ARTIST.method, artist = "BTS"))
    }

    open fun playKpop(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_TAG.method, tag = "kpop"))
    }

    open fun playBillieEilish(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_ARTIST.method, artist = "Billie Eilish"))
    }

}
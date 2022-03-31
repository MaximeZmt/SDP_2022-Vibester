package ch.sdp.vibester

//import ch.sdp.vibester.api.LastfmHelper
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.helper.GameManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity to show the list of songs for a chosen tag
 */
class GenreTemporary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_temporary)
    }

    /**
     * Fetch data from Lastfm and show song list in a ListView
     */
    private fun performQuery(uri: LastfmUri) {

        val service = LastfmApiInterface.createLastfmService()
        val call = service.getSongList(uri.convertToHashmap())
        call.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable?) {}

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                switchToGame(Gson().toJson(response.body()), uri.method)
            }
        })
    }

    fun switchToGame(response: String, method: String) {
        val gameManager = GameManager()
        gameManager.setGameSongList(response, method)
        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        startActivity(newIntent)
    }

    // view is needed, remove it will result failure in tests
    fun playRock(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_TAG.method, tag = "rock"))
    }

    fun playImagineDragons(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_ARTIST.method, artist = "Imagine Dragons"))
    }

    fun playTopTracks(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_CHART.method))
    }

    fun playBTS(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_ARTIST.method, artist = "BTS"))
    }

    fun playKpop(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_TAG.method, tag = "kpop"))
    }

    fun playBillieEilish(view: View) {
        performQuery(LastfmUri(method = LastfmMethod.BY_ARTIST.method, artist = "Billie Eilish"))
    }

}
package ch.sdp.vibester

//import ch.sdp.vibester.api.LastfmHelper
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Lyric
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//        gameManager.setGameSongList(LastfmApi.querySongList(OkHttpClient(),uri).get(),uri.method)
//        val newIntent = Intent(this, TypingGameActivity::class.java)
//        newIntent.putExtra("gameManager", gameManager)
//        startActivity(newIntent)
        val service = LastfmApiInterface.create()
        val paramsMap: MutableMap<String, String> = HashMap()
        paramsMap["method"] = uri.method
        paramsMap["api_key"] = "52bfdc690dd8373bba5351571a01ac14"
        paramsMap["format"] = uri.format
        paramsMap["page"] = uri.page
        paramsMap["tag"] = uri.tag
        paramsMap["limit"] = uri.limit
        paramsMap["artist"] = uri.artist

//        val call = service.getSongList(uri.format, uri.method,"52bfdc690dd8373bba5351571a01ac14",uri.tag,uri.artist  )
        val call = service.getSongList(paramsMap)
        call.enqueue(object: Callback<Object> {
            override fun onFailure(call: Call<Object>, t: Throwable?) {}

            override fun onResponse(call: Call<Object>, response: Response<Object>) {
                if (response != null) {
                    switchToGame(Gson().toJson(response.body()), uri.method)
                }
            }
        })
//        Thread.sleep(5000)
    }
    fun switchToGame(response: String, method:String){
        val gameManager = GameManager()
        gameManager.setGameSongList(response, method)
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
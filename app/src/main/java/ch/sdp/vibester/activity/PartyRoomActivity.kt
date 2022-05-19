package ch.sdp.vibester.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.helper.ArtistIdentifier
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class PartyRoomActivity : AppCompatActivity() {
    @Inject
    lateinit var dataGetter: DataGetter

    lateinit var gameManager: GameManager

    var gameStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_room)

        val roomName = intent.getStringExtra("roomName").toString()
        val createPartyRoom = intent.getBooleanExtra("createRoom", false)
        if(createPartyRoom) {
            createRoom(roomName)
        }
        else {
            fetchData(roomName)
        }

        val startGame = findViewById<Button>(R.id.startGame)

        startGame.setOnClickListener {
//            dataGetter.dowloadSongList(roomName, this::launchGame)
            setGameManager()
//            dataGetter.updateSongList("room1", gameManager.getSongList())
        }
    }

    private fun updateUI(partyRoom: PartyRoom) {
        findViewById<TextView>(R.id.emails).text = partyRoom.getEmailList().toString()
    }

    private fun fetchData(roomName: String) {
        dataGetter.getRoomData(roomName, this::updateUI)
    }

    private fun createRoom(roomName: String) {
        dataGetter.createRoom(roomName, this::updateUI)
    }

    private fun setGameManager() {
        gameManager = GameManager()
        gameManager.setGameSize(1)

        chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Imagine Dragons", mode = R.string.imagine_dragons)
    }

    private fun test() {
        Log.w("DEBUG LMAO", "HELLO HELLO")
    }

    private fun launchGame(newGameManager: GameManager) {

        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("gameManager", newGameManager)
        newIntent.putExtra("Difficulty", R.string.easy.toString())

        startActivity(newIntent)
    }

    private fun setGameSongList(uri: LastfmUri) {
        val service = LastfmApiInterface.createLastfmService()
        val call = service.getSongList(uri.convertToHashmap())
        call.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable?) {}
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                /* TODO: OFFLINE
                gameManager.setInternet()
                gameManager.setContext(context)
                 */


                gameManager.setGameSongList(Gson().toJson(response.body()), uri.method)
                Log.w("DEBUG", gameManager.getSongList().toString())
                launchGame(gameManager)
            }
        })
    }

    private fun chooseGenre(method: String = "", artist: String = "", tag: String = "", mode: Int = 0) {
        val uri = LastfmUri()

        uri.method = method
        uri.artist = artist
        uri.tag = tag

        gameManager.gameMode = getString(mode)
        AppPreferences.setStr(getString(R.string.preferences_game_genre), getString(mode))

        setGameSongList(uri)
    }

    private fun dowloadSongList() {

    }
}
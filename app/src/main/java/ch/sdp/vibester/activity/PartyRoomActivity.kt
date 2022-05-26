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

    lateinit var roomID: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_room)

        val createPartyRoom = intent.getBooleanExtra("createRoom", false)
        if(createPartyRoom) {
            createRoom()
            setGameManager()
            dataGetter.updateRoomField(roomID, "gameStarted", false)
        }
        else {
            this.roomID = intent.getStringExtra("roomID").toString()
            fetchData(roomID)
        }

        val startGame = findViewById<Button>(R.id.startGame)

        startGame.setOnClickListener {
//            dataGetter.updateStartGame(roomID, true)
            dataGetter.updateRoomField(roomID, "gameStarted", true)
        }

        fetchGameStarted(roomID, this::startGame)
    }

    private fun updateUI(partyRoom: PartyRoom, roomID: String) {
        this.roomID = roomID
        findViewById<TextView>(R.id.emails).text = partyRoom.getEmailList().toString()
        findViewById<TextView>(R.id.roomId).text = roomID
    }

    private fun fetchData(roomID: String) {
        dataGetter.getRoomData(roomID, this::updateUI, this::setSongs)
    }

    private fun createRoom() {
        dataGetter.createRoom(this::updateUI)
    }

    private fun fetchGameStarted(roomName: String, callback: (Boolean) -> Unit) {
        dataGetter.readStartGame(roomName, callback)
    }

    private fun startGame(gameStarted: Boolean) {
        if(gameStarted) {
            launchGame(gameManager)
        }
    }

    private fun  setGameManager() {
        gameManager = GameManager()
        gameManager.gameSize = 1

        chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Imagine Dragons", mode = R.string.gameGenre_imagine_dragons)
    }


    private fun launchGame(newGameManager: GameManager) {

        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("gameManager", newGameManager)
        newIntent.putExtra("Difficulty", R.string.GameSetup_easy)

        startActivity(newIntent)
    }

    private fun setGameSongList(uri: LastfmUri, roomID: String) {
        val service = LastfmApiInterface.createLastfmService()
        val call = service.getSongList(uri.convertToHashmap())
        call.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable?) {}
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                gameManager.setGameSongList(Gson().toJson(response.body()), uri.method)
                dataGetter.updateRoomField(roomID, "songList", gameManager.getSongList())
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

        setGameSongList(uri, roomID)
    }

    private fun setSongs(gameSongList: MutableList<Pair<String, String>>) {
        gameManager = GameManager()
        gameManager.gameSize = 1
        gameManager.gameMode = getString(R.string.gameGenre_imagine_dragons)

        gameManager.gameSongList = gameSongList

    }

}
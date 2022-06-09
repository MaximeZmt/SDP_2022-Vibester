package ch.sdp.vibester.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.game.TypingGameActivity
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.PartyRoom
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class PartyRoomActivity : AppCompatActivity() {
    @Inject
    lateinit var dataGetter: DataGetter

    lateinit var gameManager: GameManager

    lateinit var roomID: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_room)

        val joinRoom = intent.getBooleanExtra("joinRoom", false)
        if (!joinRoom) {
            gameManager = intent.getSerializableExtra("gameManager") as GameManager
            AppPreferences.setStr(getString(R.string.preferences_game_genre), gameManager.gameMode)
            createRoom()
            setGameManager()
        }
        else {
            this.roomID = intent.getStringExtra("roomID").toString()
            fetchData(roomID)
        }

        val startGame = findViewById<Button>(R.id.startGame)

        startGame.setOnClickListener {

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
        dataGetter.getRoomData(roomID, this::updateUI, this::setData)
    }

    private fun setData(gameSongList: MutableList<Pair<String, String>>, gameSize: Int, gameMode: String, difficultyLevel: Int) {
        gameManager = GameManager()

        gameManager.gameSongList = gameSongList
        gameManager.gameSize = gameSize
        gameManager.gameMode = gameMode
        gameManager.difficultyLevel = difficultyLevel

        AppPreferences.setStr(getString(R.string.preferences_game_genre), gameManager.gameMode)
    }

    private fun createRoom() {
        dataGetter.createRoom(this::updateUI)
    }

    private fun setGameManager() {
        dataGetter.updateRoomField(roomID, "songList", gameManager.getSongList())
        dataGetter.updateRoomField(roomID, "difficulty", gameManager.difficultyLevel)
        dataGetter.updateRoomField(roomID, "gameSize", gameManager.gameSize)
        dataGetter.updateRoomField(roomID, "gameMode", gameManager.gameMode)
    }

    private fun fetchGameStarted(roomName: String, callback: (Boolean) -> Unit) {
        dataGetter.readStartGame(roomName, callback)
    }

    private fun startGame(gameStarted: Boolean) {
        if(gameStarted) {
            launchGame(gameManager)
        }
    }

    private fun launchGame(newGameManager: GameManager) {
        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("gameManager", newGameManager)
        newIntent.putExtra("Difficulty", newGameManager.difficultyLevel)

        newIntent.putExtra("onlineGame", true)
        newIntent.putExtra("userEmail", dataGetter.getCurrentUser()?.email)
        newIntent.putExtra("roomID", roomID)

        startActivity(newIntent)
    }

}
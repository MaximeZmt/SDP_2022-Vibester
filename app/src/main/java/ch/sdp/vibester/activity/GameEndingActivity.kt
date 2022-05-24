package ch.sdp.vibester.activity

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongListAdapter
import ch.sdp.vibester.user.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.OkHttpClient
import java.io.File
import java.lang.IllegalArgumentException

/**
 * Game ending activity with game stats and list of songs quessed correctly/wrong
 */
class GameEndingActivity : AppCompatActivity(), OnItemClickListener {

    private val endStatArrayList = arrayListOf(R.id.end_stat1, R.id.end_stat2, R.id.end_stat3, R.id.end_stat4)

    private var incorrectSongList: ArrayList<String> = arrayListOf()
    private var correctSongList: ArrayList<String> = arrayListOf()
    private var statNames: ArrayList<String> = arrayListOf()
    private var statValues: ArrayList<String> = arrayListOf()

    lateinit var songListAdapter: SongListAdapter
    private var recyclerView: RecyclerView? = null

    //Companion object to indicate when the download completes.
    companion object {
        var downloadComplete = false
        var downloadStarted = false
    }

    private val STORAGE_PERMISSION_CODE = 1000
    private lateinit var song: Song
    private lateinit var songName: String
    private var downloadId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val gameMode = AppPreferences.getStr(getString(R.string.preferences_game_mode))
        if (gameMode == "local_typing" || gameMode == "local_lyrics") {
            setContentView(R.layout.activity_end_solo)
            getFromIntentSolo(intent)
        }
        else {
            setContentView(R.layout.activity_end_multiple)
            getFromIntentMultiple(intent)
        }

        setGameMode()

        setUpRecyclerView()

        Helper().setReturnToMainListener(findViewById<FloatingActionButton>(R.id.end_returnToMain), this)

        val broadcast = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    alert(getString(R.string.download_download_complete))
                }
            }
        }
        registerReceiver(broadcast, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.end_song_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        songListAdapter = SongListAdapter(incorrectSongList, correctSongList, this)
        recyclerView!!.adapter = songListAdapter
    }

    /**
     * Set text for game mode
     */
    private fun setGameMode(){
        val gameMode = AppPreferences.getStr(getString(R.string.preferences_game_mode))?.replace("_", " ")?.replaceFirstChar { it.uppercase() }
        val gameGenre = AppPreferences.getStr(getString(R.string.preferences_game_genre))
        findViewById<TextView>(R.id.end_game_mode).text = "$gameMode - $gameGenre"
    }

    /**
     * Set statistics for Solo Game
     */
    private fun setSoloStats() {
        val stat1: TextView = findViewById(R.id.end_stat1)
        stat1.text = statNames[0]
        val stat1res: TextView = findViewById(R.id.end_stat1_res)
        stat1res.text = statValues[0]
    }

    /**
     * Handle intent values for solo game
     * @param intent: intent received by the activity
     */
    private fun getFromIntentSolo(intent: Intent) {
        incorrectSongList = intent.getStringArrayListExtra("incorrectSongList") as ArrayList<String>
        correctSongList = intent.getStringArrayListExtra("correctSongList") as ArrayList<String>

        statNames = intent.getStringArrayListExtra("statNames") as ArrayList<String>
        statValues = intent.getStringArrayListExtra("statValues") as ArrayList<String>
        setSoloStats()
    }

    /**
     * Creates a text view with a given text and gravity
     * @param text: the text to be put in the view
     * @param gravity: the desired gravity of the view
     * @return the created view
     */
    private fun createTextView(text: String, gravity: Int, row: TableRow) {
        val view = TextView(this)
        view.text = text
        view.height = 100
        view.width = 300
        view.textAlignment = View.TEXT_ALIGNMENT_CENTER
        view.fontFeatureSettings = "monospace"
        view.textSize = 20f
        view.gravity = gravity
        row.addView(view)
    }

    /**
     * Handle intent values for multiple players game
     * @param intent: intent received by the activity
     */
    private fun getFromIntentMultiple(intent: Intent) {
        if (intent.hasExtra("Winner Name")) {
            val winner = intent.getStringExtra("Winner Name")
            findViewById<TextView>(R.id.winnerText).text = winner
        }

        if (intent.hasExtra("Player Scores")) {
            val playerScores =
                intent.getSerializableExtra("Player Scores")!! as HashMap<String, Int>
            var i = 0
            for (pName in playerScores.keys) {
                val row = findViewById<TableRow>(endStatArrayList[i])
                row.visibility = View.VISIBLE

                createTextView(pName, Gravity.LEFT, row)
                createTextView(playerScores[pName]!!.toString(), Gravity.RIGHT, row)

                i += 1
            }
        }
    }

    override fun onItemClick(position: Int) {
        val songList = incorrectSongList + correctSongList
        downloadListener(songList[position])
    }

    private fun alert(toast: String) {
        downloadComplete = true
        downloadStarted = false
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }

    /**
     * Function that handles deletion button pushes.
     */
    private fun downloadListener(songName0: String) {
        if (downloadStarted) {
            Toast.makeText(applicationContext, getString(R.string.download_already_downloading), Toast.LENGTH_LONG).show()
        } else {
            downloadStarted = true
            downloadComplete = false
            songName = songName0

            if (checkExistingSong()) {
                alert(getString(R.string.download_already_done))
            } else { getAndDownload() }
        }
    }

    private fun checkExistingSong(): Boolean {
        val existing = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_$songName")
        return existing.exists()
    }

    private fun getAndDownload() {
        val songFuture = ItunesMusicApi.querySong(songName, OkHttpClient(), 1)
        try {
            song = Song.singleSong(songFuture.get())
            songName = song.getTrackName().lowercase() + " - " + song.getArtistName().lowercase()
            checkPermissionsAndDownload()
        } catch (e: IllegalArgumentException) {
            alert(getString(R.string.download_unable_to_find))
        }
    }

    private fun checkPermissionsAndDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <  Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            } else { downloadId = startDownload() }
        } else { downloadId = startDownload() }
    }

    private fun startDownload(): Long {
        record()
        val request = DownloadManager.Request(Uri.parse(song.getPreviewUrl()))
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_MOBILE
                    or DownloadManager.Request.NETWORK_WIFI
        )
            .setTitle("extract_of_$songName")
            .setAllowedOverRoaming(true)
            .setDescription("Downloading extract of the song + $songName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "extract_of_$songName")

        val downloader = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloader.enqueue(request)
    }

    private fun record() {
        val records = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")

        if (!records.exists()) {
            records.createNewFile()
        }

        records.appendText("$songName\n")
        recordProperties()
    }

    private fun recordProperties() {
        val properties = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")

        if (!properties.exists()) {
            properties.createNewFile()
        }

        properties.appendText("${song.getTrackName()} - ${song.getArtistName()} - ${song.getArtworkUrl()} - ${song.getPreviewUrl()}\n")
    }
}
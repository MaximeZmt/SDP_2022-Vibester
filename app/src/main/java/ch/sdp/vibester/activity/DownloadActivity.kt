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
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import org.w3c.dom.Text
import java.io.File
import java.lang.IllegalArgumentException

/**
 * Activity that handles downloading of song extracts.
 */
class DownloadActivity : AppCompatActivity() {
    //Companion object to indicate when the download completes.
    companion object {
        var downloadComplete = false
        var downloadStarted = false
    }

    private val STORAGE_PERMISSION_CODE = 1000
    private lateinit var song: Song
    private var songName: String = "imagine dragons believer"
    private var downloadId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)


        val songNameView = findViewById<TextView>(R.id.download_songName)
        val downloadButton = findViewById<Button>(R.id.download_downloadsong)


        downloadButton.setOnClickListener {
            downloadListener(songNameView)
        }

        var broadcast = object:BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    alert(
                        getString(R.string.download_download_complete),
                        getString(R.string.download_try_another),
                        songNameView
                    )
                }
            }
        }
            registerReceiver(broadcast, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }

    /**
     * Function that handles deletion button pushes.
     */
    private fun downloadListener(songView: TextView) {
        if(downloadStarted) {
            Toast.makeText(applicationContext, getString(R.string.download_already_downloading), Toast.LENGTH_LONG).show()
            editTextView(getString(R.string.download_please_retry_later), songView)
        } else {
            downloadStarted = true
            downloadComplete = false
            songName = songView.text.toString()

            if (checkExistingSong()) {
                alert(
                    getString(R.string.download_already_done),
                    getString(R.string.download_try_different),
                    songView
                )
            } else {
                val songFuture = ItunesMusicApi.querySong(songName, OkHttpClient(), 1)
                try {
                    song = Song.singleSong(songFuture.get())
                    checkPermissionsAndDownload()
                } catch (e: IllegalArgumentException) {
                    alert(
                        getString(R.string.download_unable_to_find),
                        getString(R.string.download_retry),
                        songView
                    )
                }
            }
        }
    }

    /**
     * Displays a Toast on the screen while editing the existing textView.
     *
     * @param toast: String to be displayed on the Toast.
     * @param hint : String to be set as the hint of the textView.
     * @param view : The textView that will be updated.
     */
    private fun alert(toast: String, hint: String, view: TextView) {
        downloadComplete = true
        downloadStarted = false
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
        editTextView(hint, view)
    }

    /**
     * Sets the hint of the given textview with the given hint, and clears the entered text.
     *
     * @param hint          : String to be set as the hint of the textView.
     * @param songNameView  : The textView that will be updated.
     */
    private fun editTextView(hint: String, songNameView: TextView) {
        songNameView.text = ""
        songNameView.hint = hint
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadId = startDownload()
            }
        } else {
            Toast.makeText(this, getString(R.string.download_permission_denied), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Checks if the required app permissions are already given. If not, request those permissions.
     */
    private fun checkPermissionsAndDownload() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            Build.VERSION.SDK_INT <  Build.VERSION_CODES.Q) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            } else {
                downloadId = startDownload()
            }
        } else {
            downloadId = startDownload()
        }
    }

    /**
     * Download a file from the private URL value of the class.
     */
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
            .setDestinationInExternalFilesDir(this,
            Environment.DIRECTORY_DOWNLOADS,
            "extract_of_$songName")

        val downloader = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloader.enqueue(request)
    }

    /**
     * Indicator of if the song already exists or not.
     */
    private fun checkExistingSong(): Boolean {
        var existing = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_$songName")
        return existing.exists()
    }

    /**
     * Helps keep track of the currently downloaded songs in the form of a txt file.
     * If the file does not exist, it is created.
     */
    private fun record() {
        var records = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")

        if(!records.exists()) {
            records.createNewFile()
        }
        records.appendText("$songName\n")
    }

    fun switchToWelcome(view: View) {
        IntentSwitcher.switchBackToWelcome(this)
    }

    fun switchToDeleteSongs(view: View) {
        val intent = Intent(this, DeleteSongsActivity::class.java)
        startActivity(intent)
    }

}
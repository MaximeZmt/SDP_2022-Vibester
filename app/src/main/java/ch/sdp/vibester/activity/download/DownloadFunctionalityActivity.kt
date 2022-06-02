package ch.sdp.vibester.activity.download

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import java.io.File
import java.lang.IllegalArgumentException

/**
 * This is a class that handles downloading songs on demand. This is an AppCompatActivity instance,
 * which means all the inheritors can inherit only from this class to be fully functional at the
 * minimum.
 */
open class DownloadFunctionalityActivity : AppCompatActivity() {

    companion object {
        var downloadComplete = true
        var downloadStarted = false
    }

    private val STORAGE_PERMISSION_CODE = 1000
    private lateinit var song: Song
    private lateinit var songName: String
    private var downloadId: Long = 0

    /**
     * Asks for the requested permission code in case it is not already granted to the app.
     * @param requestCode: request code of the permission being asked
     * @param permissions: an Array of permissions to pass to the super.onRPS
     * @param grantResults: IntArray in which the results for the permission inquiries are stored
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadId = startDownload()
            }
        } else { Toast.makeText(this, getString(R.string.download_permission_denied), Toast.LENGTH_LONG).show() }
    }

    /**
     * Creates a broadcast receiver and registers it to the caller class. This receiver is used to
     * listen to actions indicating a download has been done.
     * @param songNameView: The TextView instance which will be modified in accordance to the current
     * state of the broadcast receiver. Used for DownloadActivity.
     */
    fun createDownloadReceiver(songNameView: TextView?) {
        val broadcast = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    alert(getString(R.string.download_download_complete), getString(R.string.download_try_another), songNameView)
                }
            }
        }
        registerReceiver(broadcast, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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


    /**
     * Displays a Toast on the screen while editing the existing textView.
     *
     * @param toast: String to be displayed on the Toast.
     * @param hint : String to be set as the hint of the textView.
     * @param view : The textView that will be updated.
     */
    fun alert(toast: String, hint: String?, view: TextView?) {
        downloadComplete = true
        downloadStarted = false
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
        if (hint != null && view != null) {
            editTextView(hint, view)
        }
    }

    /**
     * set up listener for download
     * @param songView TextView used for DownloadActivity
     * @param songName0 String used for GameEndingActivity
     */
    fun downloadListener(songView: TextView?, songName0: String?) {
        if (downloadStarted) {
            Toast.makeText(applicationContext, getString(R.string.download_already_downloading), Toast.LENGTH_LONG).show()
            if (songView != null) {
                editTextView(getString(R.string.download_please_retry_later), songView)
            }
        } else {
            downloadStarted = true
            downloadComplete = false
            if (songView != null ) {
                songName = songView.text.toString()
            } else if (songName0 != null) {
                songName = songName0
            }

            if (checkExistingSong()) {
                alert(getString(R.string.download_already_done), getString(R.string.download_try_different), songView)
            } else { getAndDownload(songView) }
        }
    }

    /**
     * Indicator of if the song already exists or not.
     */
    private fun checkExistingSong(): Boolean {
        val existing = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_$songName")
        return existing.exists()
    }

    /**
     * Retrieves the song to be downloaded from the ITunes API and initiates the download procedure.
     * @param songView: TextView to be edited and alerted.
     */
    private fun getAndDownload(songView: TextView?) {
        val songFuture = ItunesMusicApi.querySong(songName, OkHttpClient(), 1)
        try {
            song = Song.singleSong(songFuture.get())
            songName = song.getTrackName().lowercase() + " - " + song.getArtistName().lowercase()
            checkPermissionsAndDownload()
        } catch (e: IllegalArgumentException) {
            alert(getString(R.string.download_unable_to_find), getString(R.string.download_retry), songView)
        }
    }

    /**
     * Checks if the required app permissions are already given. If not, request those permissions.
     */
    private fun checkPermissionsAndDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <  Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            } else { downloadId = startDownload() }
        } else { downloadId = startDownload() }
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
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "extract_of_$songName")

        val downloader = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloader.enqueue(request)
    }

    /**
     * Helps keep track of the currently downloaded songs in the form of a txt file.
     * If the file does not exist, it is created.
     */
    private fun record() {
        val records = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")

        if (!records.exists()) {
            records.createNewFile()
        }

        records.appendText("$songName\n")
        recordProperties()
    }

    /**
     * Records the properties of a song.
     * Order of storage: Track name - artist name - artwork URL - preview URL.
     */
    private fun recordProperties() {
        val properties = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")

        if (!properties.exists()) {
            properties.createNewFile()
        }

        properties.appendText("${song.getTrackName()} - ${song.getArtistName()} - ${song.getArtworkUrl()} - ${song.getPreviewUrl()}\n")
    }
}
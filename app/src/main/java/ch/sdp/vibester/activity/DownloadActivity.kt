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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ch.sdp.vibester.R
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import org.w3c.dom.Text
import java.lang.IllegalArgumentException
/*
 * Activity that handles downloading of song extracts.
 */
class DownloadActivity : AppCompatActivity() {
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
            songName = songNameView.text.toString()
            val songFuture = ItunesMusicApi.querySong(songName, OkHttpClient(), 1)
            try {
                song = Song.singleSong(songFuture.get())
                checkPermissionsAndDownload()
            } catch (e: IllegalArgumentException) {
                alert("Unable to find song, please retry!", "Please retry!", songNameView)
            }
        }

        var broadcast = object:BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if(id == downloadId) {
                    alert("Download completed!", "Try another song!", songNameView)
                }
            }
        }

        registerReceiver(broadcast, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    /*
     * Displays a Toast on the screen while editing the existing textView.
     *
     * @param toast: String to be displayed on the Toast.
     * @param hint : String to be set as the hint of the textView.
     * @param view : The textView that will be updated.
     */
    private fun alert(toast: String, hint: String, view: TextView) {
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
        editTextView(hint, view)
    }

    /*
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
            Toast.makeText(this, "Permission denied, cannot download!", Toast.LENGTH_LONG).show()
        }
    }

    /*
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

    /*
     * Download a file from the private URL value of the class.
     */
    private fun startDownload(): Long {
        val request = DownloadManager.Request(Uri.parse(song.getPreviewUrl()))
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_MOBILE
                    or DownloadManager.Request.NETWORK_WIFI
        )
            .setTitle("extract_of_$songName")
            .setAllowedOverRoaming(true)
            .setDescription("Downloading extract of the song + $songName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "extract_of_$songName"
            )

        val downloader = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloader.enqueue(request)
    }
}
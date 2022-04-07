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
import java.lang.IllegalArgumentException

class DownloadActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE = 1000
    private lateinit var song: Song
    private var songName: String = "imagine dragons believer"
    private var downloadId: Long = 0
    private val songNameView = findViewById<TextView>(R.id.download_songName)
    private val downloadButton = findViewById<Button>(R.id.download_downloadsong)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        downloadButton.setOnClickListener {
            songName = songNameView.text.toString()
            val songFuture = ItunesMusicApi.querySong(songName, OkHttpClient(), 1)
            try {
                song = Song.singleSong(songFuture.get())
                checkPermissionsAndDownload()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(applicationContext, "Unable to find song, please retry!", Toast.LENGTH_LONG).show()
                editTextView("Please retry!")
            }
        }

        var broadcast = object:BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if(id == downloadId) {
                    Toast.makeText(applicationContext, "Download completed!", Toast.LENGTH_LONG).show()
                    editTextView("Try another song!")
                }
            }
        }

        registerReceiver(broadcast, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun editTextView(hint: String) {
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
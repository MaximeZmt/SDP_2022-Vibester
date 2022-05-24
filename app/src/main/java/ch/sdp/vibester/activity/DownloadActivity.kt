package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.Helper
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Activity that handles downloading of song extracts.
 */
class DownloadActivity : DownloadFunctionalityActivity() {
    //Companion object to indicate when the download completes.
    companion object {
        var downloadComplete = false
        var downloadStarted = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val songNameView = findViewById<TextView>(R.id.download_songName)
        val downloadButton = findViewById<Button>(R.id.download_downloadsong)

        downloadButton.setOnClickListener {
            downloadListener(songNameView,  null)
        }

        Helper().setReturnToMainListener(findViewById<FloatingActionButton>(R.id.download_returnToMain), this)

        createDownloadReceiver(songNameView)
    }



    fun switchToDeleteSongs(view: View) {
        val intent = Intent(this, DeleteSongsActivity::class.java)
        startActivity(intent)
    }
}
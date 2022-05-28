package ch.sdp.vibester.activity.download

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.model.SongListAdapterForDelete
import ch.sdp.vibester.user.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class DownloadManagerActivity : AppCompatActivity(), OnItemClickListener {
    private var recyclerView: RecyclerView? = null
    private lateinit var songListAdapter: SongListAdapterForDelete
    private var songList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_download_manager)

        Helper().setReturnToMainListener(
            findViewById<FloatingActionButton>(R.id.downloadManager_returnToMain),
            this
        )

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.download_song_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        songListAdapter = SongListAdapterForDelete(getDownloadedSongs(), this)
        recyclerView!!.adapter = songListAdapter
    }

    private fun getDownloadedSongs(): ArrayList<String> {
        val records = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "records.txt"
        )

        if (!records.exists() || records.length() == 0L) {
            createNoSongsView()
        } else {
            val reader = BufferedReader(FileReader(records))
            var currentLine = reader.readLine()
            var iterator = 0

            while (currentLine != null) {
                val songName = currentLine.trim()
                if (songName.isNotEmpty()) {
                    songList.add(songName)
                }
                currentLine = reader.readLine()
                iterator += 1
            }
            reader.close()
        }

        return songList
    }

    private fun deleteDownloadedSong(song: String): Boolean {
        val records = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        val tempRecords = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "temp.txt")

        val recordReader = BufferedReader(FileReader(records))
        val recordWriter = BufferedWriter(FileWriter(tempRecords, true))

        var currentLine = recordReader.readLine()

        while (currentLine != null) {
            val currentSong = currentLine.trim()

            if (currentSong == song) {
                currentLine = recordReader.readLine()
                continue
            }

            recordWriter.write(currentLine)
            recordWriter.newLine()
            currentLine = recordReader.readLine()
        }

        recordWriter.close()
        recordReader.close()

        if (tempRecords.length() == 0L) {
            createNoSongsView()
        }

        records.delete()

        return deleteResult(records, tempRecords, song)
    }

    private fun deleteResult(records: File, tempRecords: File, song: String): Boolean {
        if (tempRecords.renameTo(records) && removeFromProperties(song)) {
            val songToDelete = File(
                applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "extract_of_$song")

            if (songToDelete.delete()) {
                Toast.makeText(applicationContext, R.string.download_manager_delete_success, Toast.LENGTH_SHORT).show()
                return true
            }
        }
        Toast.makeText(applicationContext, R.string.download_manager_delete_fail, Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Removes the line corresponding to the song-to-be-deleted from the properties.txt file.
     * @param song: Text which contains song name - artist name
     */
    private fun removeFromProperties(song: String): Boolean {
        val properties = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        val tempProp = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "tempProp.txt")

        val propReader = BufferedReader(FileReader(properties))
        val propWriter = BufferedWriter(FileWriter(tempProp, true))

        var currentLine = propReader.readLine()
        val buttonSplit = song.trim().split("-")

        /*
         * Current line here should be in the form of "song name - artist name - artwork url - preview url.
         * We want to tokenize(split) this string into 4 tokens: song name, artist name, artwork url and preview url.
         * Comparison between the first two tokens are checked to see whether this line is the one we want or not.
         * We write every line on a new file except the one which we want to delete, thus we continue if we match.
         */
        while (currentLine != null) {
            val trimmed = currentLine.trim()
            val split = trimmed.split(" - ")

            if (split[0].trim().lowercase() == buttonSplit[0].trim().lowercase()
                && split[1].trim().lowercase() == buttonSplit[1].trim().lowercase()
            ) {
                currentLine = propReader.readLine()
                continue
            }

            propWriter.write(currentLine)
            propWriter.newLine()
            currentLine = propReader.readLine()
        }

        propWriter.close()
        propReader.close()

        properties.delete()
        return tempProp.renameTo(properties)
    }

    private fun createNoSongsView() {
        findViewById<TextView>(R.id.download_empty).visibility = VISIBLE
        findViewById<NestedScrollView>(R.id.download_song_list_nestedScrollView).visibility = GONE
    }


    override fun onItemClick(position: Int) {
        // TODO: remove the entire item correct
        val parentActual = findViewById<Button>(R.id.song_delete).parent as RelativeLayout
        val parentOfParent = parentActual.parent as RecyclerView

        val relative = parentOfParent.children.elementAt(position) as RelativeLayout
        val deleteButton = relative.children.elementAt(1) as Button
        if (deleteDownloadedSong(songList[position])) {
            deleteButton.visibility = View.INVISIBLE
        }

    }
}
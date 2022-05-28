package ch.sdp.vibester.activity.download

import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.Helper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

/**
 * Class that handles deleting files, more specifically songs in the scope of this project.
 */
class DeleteSongsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_delete_songs)

        Helper().setReturnToMainListener(
            findViewById<FloatingActionButton>(R.id.delete_returnToMain),
            this
        )

        val layout: LinearLayout = findViewById(R.id.delete_songs_linear)
        generateButtons(layout)
    }

    /**
     * Generates buttons for each downloaded extract to be added to the scrollview layout.
     * Assigns each button a call to the "deleteDownloadedSong" function as a listener.
     */
    private fun generateButtons(layout: LinearLayout) {
        val records = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "records.txt"
        )

        if (!records.exists() || records.length() == 0L) {
            createNoSongsView(layout)
        } else {
            val reader = BufferedReader(FileReader(records))
            var currentLine = reader.readLine()
            var iterator = 0

            while (currentLine != null) {
                val trimmed = currentLine.trim()
                if (trimmed.isNotEmpty()) {
                    val deleteButton = createButton(trimmed, iterator, layout)
                    layout.addView(deleteButton)
                }
                currentLine = reader.readLine()
                iterator += 1
            }
            reader.close()
        }
    }

    /**
     * Handles the creation and initialization of buttons.
     */
    private fun createButton(trimmed: String, iterator: Int, layout: LinearLayout): Button {
        val deleteButton = Button(this)
        deleteButton.id = iterator
        deleteButton.text = trimmed
        deleteButton.gravity = Gravity.CENTER
        deleteButton.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        deleteButton.setOnClickListener {
            deleteDownloadedSong(it, layout)
        }
        return deleteButton
    }

    /**
     * Handles the deletion of a downloaded extract. Also handles the removal of the said extract
     * from the text file which keeps track of all downloads.
     */
    private fun deleteDownloadedSong(btn: View, layout: LinearLayout): Boolean {
        val records = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "records.txt"
        )
        val tempRecords = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "temp.txt"
        )

        val recordReader = BufferedReader(FileReader(records))
        val recordWriter = BufferedWriter(FileWriter(tempRecords, true))

        var currentLine = recordReader.readLine()
        val button: Button = btn as Button
        val buttonText = button.text.toString()

        while (currentLine != null) {
            val trimmed = currentLine.trim()

            if (trimmed == buttonText) {
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
            createNoSongsView(layout)
        }

        records.delete()
        if (tempRecords.renameTo(records) && removeFromProperties(buttonText)) {
            layout.removeView(btn)
            val songToDelete = File(
                applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "extract_of_${button.text}"
            )
            if (songToDelete.delete()) {
                Toast.makeText(applicationContext, "Song successfully removed!", Toast.LENGTH_LONG).show()
                return true
            }
        }
        Toast.makeText(applicationContext, "Song was unable to be removed!", Toast.LENGTH_LONG).show()
        return false
    }

    /**
     * Removes the line corresponding to the song-to-be-deleted from the properties.txt file.
     * @param buttonText: Text which contains song name - artist name
     */
    private fun removeFromProperties(buttonText: String): Boolean {
        val properties = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "properties.txt"
        )
        val tempProp = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "tempProp.txt"
        )

        val propReader = BufferedReader(FileReader(properties))
        val propWriter = BufferedWriter(FileWriter(tempProp, true))

        var currentLine = propReader.readLine()
        val buttonSplit = buttonText.trim().split("-")

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

    /**
     * Handles the creation and initialization of a TextView which is then added to the layout.
     */
    private fun createNoSongsView(layout: LinearLayout) {
        val noDownloadsText = TextView(this)
        noDownloadsText.id = resources.getIdentifier("noDownloadsView", "id", packageName)
        noDownloadsText.text = resources.getString(R.string.delete_nothing_to_delete)
        noDownloadsText.gravity = Gravity.CENTER
        noDownloadsText.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        layout.addView(noDownloadsText)
    }
}
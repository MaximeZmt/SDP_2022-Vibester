package ch.sdp.vibester.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import android.widget.Toast
import ch.sdp.vibester.R
import org.w3c.dom.Text
import java.io.*

class DeleteSongsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_delete_songs)

        val layout: LinearLayout = findViewById(R.id.delete_songs_linear)
        generateButtons(layout)
    }

    private fun generateButtons(layout: LinearLayout) {
        var records = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")

        if(!records.exists() || records.length() == 0L) {
            createNoSongsView(layout)
        } else {
            var reader = BufferedReader(FileReader(records))
            var currentLine = reader.readLine()
            var iterator = 0

            while(currentLine != null) {
                var trimmed = currentLine.trim()
                if(trimmed.isNotEmpty()) {
                    val deleteButton = Button(this)
                    deleteButton.id = iterator //resources.getIdentifier("id_$trimmed", "id", packageName)
                    deleteButton.text = trimmed
                    deleteButton.gravity = Gravity.CENTER
                    deleteButton.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    deleteButton.setOnClickListener {
                        deleteDownloadedSong(it, layout)
                    }
                    layout.addView(deleteButton)
                }
                currentLine = reader.readLine()
                iterator += 1
            }
            reader.close()
        }
    }

    private fun deleteDownloadedSong(btn: View, layout: LinearLayout): Boolean {
        var records = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        var tempRecords = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "temp.txt")

        var recordReader = BufferedReader(FileReader(records))
        var recordWriter = BufferedWriter(FileWriter(tempRecords, true))

        var currentLine = recordReader.readLine()
        var button: Button = btn as Button

        while(currentLine != null) {
            var trimmed = currentLine.trim()

            if(trimmed == button.text.toString()) {
                currentLine = recordReader.readLine()
                continue
            }

            recordWriter.write(currentLine)
            recordWriter.newLine()
            currentLine = recordReader.readLine()
        }

        recordWriter.close()
        recordReader.close()

        if(tempRecords.length() == 0L) {
            createNoSongsView(layout)
        }

        records.delete()
        if(tempRecords.renameTo(records)) {
            layout.removeView(btn)
            var songToDelete = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_${button.text}")
            if(songToDelete.delete()) {
                Toast.makeText(applicationContext, "Song successfully removed!", Toast.LENGTH_LONG).show()
                return true
            }
        }
        Toast.makeText(applicationContext, "Song was unable to be removed!", Toast.LENGTH_LONG).show()
        return false
    }

    private fun createNoSongsView(layout: LinearLayout) {
        val noDownloadsText = TextView(this)
        noDownloadsText.id = resources.getIdentifier("noDownloadsView", "id", packageName)
        noDownloadsText.text = resources.getString(R.string.delete_nothing_to_delete)
        noDownloadsText.gravity = Gravity.CENTER
        noDownloadsText.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        layout.addView(noDownloadsText)
    }

    fun switchToWelcome(view: View) {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
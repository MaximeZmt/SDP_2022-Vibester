package ch.sdp.vibester.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.TextView
import ch.sdp.vibester.R

class IncorrectSongsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incorrect_songs_screen)

        val layout: LinearLayout = findViewById(R.id.incorrect_songs_linear)

        //val incorrectSongs: Array<String> = intent.getStringArrayExtra("incorrect_songs") as Array<String>
        val incorrect1 = intent.getStringExtra("incorrect_song_1").toString()
        val incorrect2 = intent.getStringExtra("incorrect_song_2").toString()
        val incorrect3 = intent.getStringExtra("incorrect_song_3").toString()
        val incorrectSongs: Array<String> = arrayOf(incorrect1, incorrect2, incorrect3)
        val nbIncorrect: Int = intent.getIntExtra("nb_false", 0)

        if(nbIncorrect != 0) {

            for(x in 0 until nbIncorrect) {
                val textView = TextView(this)
                val resNb: Int = (x+1)
                val resName: String = "incorrect_song_$resNb"
                textView.id = resources.getIdentifier(resName, "id", packageName)
                textView.text = incorrectSongs[x]
                textView.gravity = Gravity.CENTER
                textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                layout.addView(textView)
            }
        } else {
            val textView = TextView(this)
            textView.id = resources.getIdentifier("incorrect_song_1", "id", packageName)
            textView.text = resources.getString(R.string.inc_all_correct)
            textView.gravity = Gravity.CENTER
            textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layout.addView(textView)
        }

    }

    fun switchBackToWelcome(view: View) {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.IntentSwitcher

/**
 * A class representing the activity which shows the list of songs the user
 * guessed incorrectly during the game.
 */
class IncorrectSongsActivity : AppCompatActivity() {
    private var incorrectSongs: ArrayList<String>? = ArrayList(15) //15 being max incorrect shown
    private var nbIncorrect: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_incorrect_songs_screen)

        val layout: LinearLayout = findViewById(R.id.incorrect_songs_linear)

        if (intent.hasExtra("str_arr_inc")) {
            incorrectSongs = intent.getStringArrayListExtra("str_arr_inc")
        }
        nbIncorrect = intent.getIntExtra("nb_false", 0)

        generateTextView(nbIncorrect, incorrectSongs, layout)
    }

    /**
     * A function user to generate and initialize a TextView for a nbIncorrect number of times.
     * Adds each TextView to the LinearLayout provided in the arguments.
     */
    private fun generateTextView(
        nbIncorrect: Int,
        incorrectSongs: ArrayList<String>?,
        layout: LinearLayout
    ) {
        if (nbIncorrect != 0) {

            for (x in 0 until nbIncorrect) {
                val textView = TextView(this)
                val resNb: Int = (x + 1)
                val resName = "incorrect_song_$resNb"
                textView.id = resources.getIdentifier(resName, "id", packageName)
                textView.text = incorrectSongs?.get(x)
                textView.gravity = Gravity.CENTER
                textView.layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                layout.addView(textView)
            }
        } else {
            val textView = TextView(this)
            textView.id = resources.getIdentifier("incorrect_song_1", "id", packageName)
            textView.text = resources.getString(R.string.inc_all_correct)
            textView.gravity = Gravity.CENTER
            textView.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layout.addView(textView)
        }
    }

    fun switchBackToWelcome(view: View) {
        IntentSwitcher.switchBackToWelcome(this)
    }
}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R

/**
 * A class representing the activity which appears upon
 * completion of a game. Shows various stats.
 */
class GameEndingActivity : AppCompatActivity() {


    private var incorrectSongs: ArrayList<String>? = arrayListOf("Default song")
    private var statNames: ArrayList<String>? = arrayListOf(
        "Default name 1",
        "Default name 2",
        "Default name 3",
        "Default name 4",
        "Default name 5"
    )
    private var statValues: ArrayList<String>? = arrayListOf(
        "Default value 1",
        "Default value 2",
        "Default value 3",
        "Default value 4",
        "Default value 5"
    )
    private var nbIncorrectSongs: Int = 0
    private var playerName: String? = "Default"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_game_ending_screen)

        


        if (intent.hasExtra("Winner Name")) {
            val winner = intent.getStringExtra("Winner Name")
            if (winner!=null) {
                findViewById<TextView>(R.id.winnerText).text="And the winner is... $winner!"
            } else {findViewById<TextView>(R.id.winnerText).text="Nobody won this game!"}
        }


        if (intent.hasExtra("playerName")) {
            playerName = intent.getStringExtra("playerName")
        }

        if (intent.hasExtra("nbIncorrectSong")) {
            nbIncorrectSongs = intent.getIntExtra("nbIncorrectSong", 0)
        }

        if (intent.hasExtra("str_arr_inc")) {
            incorrectSongs = intent.getStringArrayListExtra("str_arr_inc")
        }

        if (intent.hasExtra("str_arr_name")) {
            statNames = intent.getStringArrayListExtra("str_arr_name")
        }

        if (intent.hasExtra("str_arr_val")) {
            statValues = intent.getStringArrayListExtra("str_arr_val")
        }

        val stat1: TextView = findViewById(R.id.end_stat1)
        stat1.text = statNames?.get(0)
        val stat1res: TextView = findViewById(R.id.end_stat1_res)
        stat1res.text = statValues?.get(0)

        val stat2: TextView = findViewById(R.id.end_stat2)
        stat2.text = statNames?.get(1)
        val stat2res: TextView = findViewById(R.id.end_stat2_res)
        stat2res.text = statValues?.get(1)

        val stat3: TextView = findViewById(R.id.end_stat3)
        stat3.text = statNames?.get(2)
        val stat3res: TextView = findViewById(R.id.end_stat3_res)
        stat3res.text = statValues?.get(2)

        val stat4: TextView = findViewById(R.id.end_stat4)
        stat4.text = statNames?.get(3)
        val stat4res: TextView = findViewById(R.id.end_stat4_res)
        stat4res.text = statValues?.get(3)

        val stat5: TextView = findViewById(R.id.end_stat5)
        stat5.text = statNames?.get(4)
        val stat5res: TextView = findViewById(R.id.end_stat5_res)
        stat5res.text = statValues?.get(4)

        val playerNameView: TextView = findViewById(R.id.end_player_name)
        val statPlayerText = "Here are the stats for the player $playerName"
        playerNameView.text = statPlayerText
    }



    fun goToIncorrectlyGuessedSongs(view: View) {
        val intent = Intent(this, IncorrectSongsActivity::class.java)
        intent.putExtra("nb_false", nbIncorrectSongs)
        intent.putStringArrayListExtra("str_arr_inc", incorrectSongs)
        startActivity(intent)
    }


}
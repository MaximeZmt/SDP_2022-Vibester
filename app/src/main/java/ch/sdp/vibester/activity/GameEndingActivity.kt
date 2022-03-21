package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R

class GameEndingActivity : AppCompatActivity() {
    /*
    private var statNames: Array<String> = arrayOf("")
    private var statValues: Array<String> = arrayOf("")
    private var incorrectSongs: Array<String> = arrayOf("")
    */

    private var nbIncorrectSongs: Int = 0
    private var playerName: String? = ""

    //Brute-force singular variables. Unable to solve the issue with array intents.
    private var incorrect1 = ""
    private var incorrect2 = ""
    private var incorrect3 = ""
    private var stat1name = ""
    private var stat1resName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ending_screen)

        playerName = intent.getStringExtra("playerName")
        nbIncorrectSongs = intent.getIntExtra("nbIncorrectSong", 0)

        /*
        statNames = intent.getStringArrayExtra("Stat_names") as Array<String>
        statValues = intent.getStringArrayExtra("Stat_values") as Array<String>
        incorrectSongs = intent.getStringArrayExtra("incorrect_songs") as Array<String>
         */

        incorrect1 = intent.getStringExtra("incorrect_song_1").toString()
        incorrect2 = intent.getStringExtra("incorrect_song_2").toString()
        incorrect3 = intent.getStringExtra("incorrect_song_3").toString()
        stat1name = intent.getStringExtra("stat_1").toString()
        stat1resName = intent.getStringExtra("stat_res_1").toString()



        //--------------- Example setup ---------------
        // Could be done by assigning stat names in the XML file and
        // filling the values here.
        val stat1: TextView = findViewById(R.id.end_stat1)
        stat1.text = stat1name //stat1.text = statNames[0]
        val stat1res: TextView = findViewById(R.id.end_stat1_res)
        stat1res.text = stat1resName //stat1res.text = statValues[0]
        //--------------- Example setup ---------------


        val playerNameView: TextView = findViewById(R.id.end_player_name)
        val statPlayerText = "Here are the stats for the player $playerName"
        playerNameView.text = statPlayerText
    }


    fun goToIncorrectlyGuessedSongs(view: View) {
        val intent = Intent(this, IncorrectSongsActivity::class.java)
        intent.putExtra("nb_false", nbIncorrectSongs)
        //intent.putExtra("incorrect_songs", incorrectSongs)
        intent.putExtra("incorrect_song_1", incorrect1)
        intent.putExtra("incorrect_song_2", incorrect2)
        intent.putExtra("incorrect_song_3", incorrect3)
        startActivity(intent)
    }


}
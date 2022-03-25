package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import ch.sdp.vibester.R

class GameSetupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var text = "One"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_setup_screen)

        val spinner: Spinner = findViewById(R.id.nb_player_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.nb_players,
            android.R.layout.simple_spinner_item
        ).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        text = parent.getItemAtPosition(position).toString()
        updatePlayerNameVisibility(text)

        // update linear layout's visibility, add linear layout with certain visible number of rows
        // or just make 4 rows at first and update that later
    }

    // this function WILL need refactoring
    /**
    Updates visibility of player name entry fields according to number of players selected in the spinner
    @Param
    text: number of players selected in the spinner
     */
    fun updatePlayerNameVisibility(text: String) {
        when(text) {
            "One" -> {
                findViewById<EditText>(R.id.namePlayer2).visibility = android.view.View.INVISIBLE
                findViewById<EditText>(R.id.namePlayer3).visibility = android.view.View.INVISIBLE
                findViewById<EditText>(R.id.namePlayer4).visibility = android.view.View.INVISIBLE
            }
            "Two" -> {
                findViewById<EditText>(R.id.namePlayer2).visibility = android.view.View.VISIBLE
                findViewById<EditText>(R.id.namePlayer3).visibility = android.view.View.INVISIBLE
                findViewById<EditText>(R.id.namePlayer4).visibility = android.view.View.INVISIBLE
            }

            "Three" -> {
                findViewById<EditText>(R.id.namePlayer2).visibility = android.view.View.VISIBLE
                findViewById<EditText>(R.id.namePlayer3).visibility = android.view.View.VISIBLE
                findViewById<EditText>(R.id.namePlayer4).visibility = android.view.View.INVISIBLE
            }
            "Four" -> {
                findViewById<EditText>(R.id.namePlayer2).visibility = android.view.View.VISIBLE
                findViewById<EditText>(R.id.namePlayer3).visibility = android.view.View.VISIBLE
                findViewById<EditText>(R.id.namePlayer4).visibility = android.view.View.VISIBLE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {text = "One"}

    fun proceedToGame(view: View) { //FILLER INTENT
        val intent = Intent(this, GamescreenActivity::class.java)
        //intent.putExtra("Number of players", text)
        val players = findViewById<LinearLayout>(R.id.playerNames).children.filter { child: View -> child.visibility==android.view.View.VISIBLE }
        val pNameArray = arrayOfNulls<String>(players.count())
        if (players.count()>0) {
            intent.putExtra("Number of players", players.count())
        } else {intent.putExtra("Number of players", 1)}
        val editTextIdArray = arrayOf(R.id.namePlayer1, R.id.namePlayer2, R.id.namePlayer3, R.id.namePlayer4)
        var i = 0
        for (playerView in players) {
            pNameArray[i] = findViewById<EditText>(editTextIdArray[i]).text.toString()
            i = i + 1
        }
        intent.putExtra("Player Names", pNameArray)
        startActivity(intent)
    }
}
package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import ch.sdp.vibester.R

/**
 * Class to set up buzzer game (number of players)
 */
class BuzzerSetupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var text = "One"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buzzer_setup)
        val ctx: Context = this

        val spinner: Spinner = findViewById(R.id.nb_player_spinner)
        val adapter = ArrayAdapter.createFromResource(
            ctx,
            R.array.nb_players,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.onItemSelectedListener = this

        findViewById<Button>(R.id.missingNameOk).setOnClickListener {
            findViewById<LinearLayout>(R.id.missingNameAlert).visibility=View.INVISIBLE
            findViewById<Button>(R.id.nb_players_selected).visibility=View.VISIBLE
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        text = parent.getItemAtPosition(position).toString()
        updatePlayerNameVisibility(textToNumber(text), R.id.namePlayer2)
        updatePlayerNameVisibility(textToNumber(text), R.id.namePlayer3)
        updatePlayerNameVisibility(textToNumber(text), R.id.namePlayer4)
    }

    /**
     * Converts the spinner text for the number of players into an Int
     * @param
     
     */
    fun textToNumber(text: String): Int {
        when (text) {
            "One" -> return 1
            "Two" -> return 2
            "Three" -> return 3
            "Four" -> return 4
        }
        return 1
    }


    /**
     * Updates visibility of player name entry fields according to number of players selected in the spinner
     * @param
     * n: number of players selected in the spinner
     * id: the id of the field to update
     */
    fun updatePlayerNameVisibility(n: Int, id: Int) {
        var i = when (id) {
            R.id.namePlayer2 -> 2
            R.id.namePlayer3 -> 3
            R.id.namePlayer4 -> 4
            else -> 0
        }
        findViewById<EditText>(id).visibility =
            if (n >= i) View.VISIBLE else View.INVISIBLE
    }

    override fun onNothingSelected(parent: AdapterView<*>) {text = "One"}


    fun switchToGame(view: View) {
        val intent = Intent(this, BuzzerScreenActivity::class.java)
        val players =
            findViewById<LinearLayout>(R.id.playerNames).children.filter { child: View -> child.visibility == android.view.View.VISIBLE }
        val pNameArray = arrayOfNulls<String>(players.count())
        if (players.count() > 0) {
            intent.putExtra("Number of players", players.count())
        } else {
            intent.putExtra("Number of players", 1)
        }
        val editTextIdArray =
            arrayOf(R.id.namePlayer1, R.id.namePlayer2, R.id.namePlayer3, R.id.namePlayer4)
        var i = 0
        for (playerView in players) {
            val name = findViewById<EditText>(editTextIdArray[i]).text.toString()
            if (name.isNotEmpty()) { pNameArray[i] = name } else {
                findViewById<LinearLayout>(R.id.missingNameAlert).visibility=View.VISIBLE
                findViewById<Button>(R.id.nb_players_selected).visibility=View.INVISIBLE
                return }
            i += 1
        }
        intent.putExtra("Player Names", pNameArray)
        startActivity(intent)
    }

}
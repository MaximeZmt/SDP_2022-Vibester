package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.children
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.SpinnerOperation

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
     * text: the string to be converted
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
            if (n >= i) android.view.View.VISIBLE else android.view.View.INVISIBLE
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
            pNameArray[i] = findViewById<EditText>(editTextIdArray[i]).text.toString()
            i = i + 1
        }
        intent.putExtra("Player Names", pNameArray)
        startActivity(intent)
    }

}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
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

        // update linear layout's visibility, add linear layout with certain visible number of rows
        // or just make 4 rows at first and update that later
    }

    override fun onNothingSelected(parent: AdapterView<*>) {text = "One"}

    fun proceedToGame(view: View) { //FILLER INTENT
        val intent = Intent(this, GamescreenActivity::class.java)
        intent.putExtra("Number of players", text)
        startActivity(intent)
    }
}
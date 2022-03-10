package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class GameSetupScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {
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
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        text = parent.getItemAtPosition(0).toString()
    }

    fun proceedToGame(view: View) { //FILLER INTENT
        val intent = Intent(this, WelcomeScreen::class.java)
        intent.putExtra("Number of players", text)
        startActivity(intent)
    }
}
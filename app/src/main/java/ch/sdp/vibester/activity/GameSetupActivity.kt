package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.DisplayContents

class GameSetupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var text = "One"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_game_setup_screen)

        choosingGameSetupListener()

        val spinner: Spinner = findViewById(R.id.nb_player_spinner)
        spinner.background = DisplayContents.borderGen(this, R.color.floral_white)
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

    override fun onNothingSelected(parent: AdapterView<*>) {text = "One"}

    fun proceedToGame(view: View) { //FILLER INTENT
        val intent = Intent(this, GamescreenActivity::class.java)
        intent.putExtra("Number of players", text)
        startActivity(intent)
    }

    private fun choosingGameSetupListener(){
        val butBuzz = findViewById<Button>(R.id.local_buzzer_game_button)
        butBuzz.setOnClickListener({
            val chooseLinLay = findViewById<LinearLayout>(R.id.chooseGame)
            val buzzereConsLay = findViewById<ConstraintLayout>(R.id.buzzerSetup)
            chooseLinLay.visibility = GONE
            buzzereConsLay.visibility = VISIBLE
        })
    }

}
package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import ch.sdp.vibester.R

class GameSetupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var text = "One"
    var difficulty = "Easy"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_game_setup_screen)

        chooseListener(R.id.local_buzzer_game_button,
            findViewById<LinearLayout>(R.id.chooseGame),
            findViewById<ConstraintLayout>(R.id.chooseDifficulty))

        chooseListener(R.id.difficulty_proceed,
            findViewById<ConstraintLayout>(R.id.chooseDifficulty),
            findViewById<ConstraintLayout>(R.id.buzzerSetup))


        val spinnerDifficulty: Spinner = findViewById(R.id.difficulty_spinner)
        initSpinner(spinnerDifficulty, R.array.difficulties_name)
    }

    private fun initSpinner(spinner: Spinner, spinner_array: Int) {
        ArrayAdapter.createFromResource(
            this,
            spinner_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when(difficulty) {
            "Easy"      -> setDifficultyText(R.string.difficulty_easy)
            "Medium"    -> setDifficultyText(R.string.difficulty_medium)
            "Hard"      -> setDifficultyText(R.string.difficulty_hard)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {difficulty = "Easy"}


    fun proceedToGame(view: View) { //FILLER INTENT
        val intent = Intent(this, GamescreenActivity::class.java)
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
        intent.putExtra("Difficulty", difficulty)
        startActivity(intent)
    }

    private fun chooseListener(buttonId: Int, currentLayout: ViewGroup, nextLayout: ViewGroup) {
        val btn = findViewById<Button>(buttonId)
        btn.setOnClickListener {
            currentLayout.visibility = GONE
            nextLayout.visibility = VISIBLE
        }
    }

    private fun setDifficultyText(mode: Int) {
        findViewById<TextView>(R.id.difficulty_explanation).setText(mode)
    }

}
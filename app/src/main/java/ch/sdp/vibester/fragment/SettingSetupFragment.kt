package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.BuzzerSetupActivity
import ch.sdp.vibester.activity.LyricsBelongGameActivity
import ch.sdp.vibester.activity.PartyRoomActivity
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingSetupFragment:Fragment(R.layout.fragment_layout_setting), AdapterView.OnItemSelectedListener {

    private var difficulty = R.string.GameSetup_easy.toString()
    private var gameSize = R.string.one.toString()
    lateinit var gameManager: GameManager

    private var vmSettSetup = ViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmSettSetup.view = view
        vmSettSetup.ctx = view.context

        val bundle = this.arguments
        if (bundle != null) {
            gameManager = bundle.get("gameManager") as GameManager
        }

        vmSettSetup.view.findViewById<Button>(R.id.difficulty_proceed).setOnClickListener{ proceedGame() }

        setReturnBtnListener()
        setSpinnerListener(R.id.difficulty_spinner, R.array.difficulties_name)
        setSpinnerListener(R.id.size_spinner, R.array.game_size_options)
    }

    private fun setReturnBtnListener() {
        vmSettSetup.view.findViewById<FloatingActionButton>(R.id.gameSetting_returnToMain).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Switch to an activity without any extras in intent.
     */
    private fun switchToGameNoParameters(nextActivity: AppCompatActivity) {
        val newIntent = Intent(activity, nextActivity::class.java)
        startActivity(newIntent)
    }

    /**
     * Switch to an activity with extras in intent.
     */
    private fun switchToGameWithParameters(nextActivity: AppCompatActivity) {

        val newIntent = Intent(activity, nextActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        newIntent.putExtra("Difficulty", difficulty)
        startActivity(newIntent)
    }

    private fun setSpinnerListener(spinnerId: Int, resourceId: Int) {
        val spinner: Spinner = vmSettSetup.view.findViewById(spinnerId)
        val adapter = ArrayAdapter.createFromResource(
            vmSettSetup.ctx, resourceId, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        if (::gameManager.isInitialized) {
            if (parent.id == R.id.difficulty_spinner) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Easy" -> gameManager.difficultyLevel = 1
                    "Medium" -> gameManager.difficultyLevel = 2
                    "Hard" -> gameManager.difficultyLevel = 3
                }
            } else if (parent.id == R.id.size_spinner) {
                when (parent.getItemAtPosition(position).toString()) {
                    "One" -> gameManager.gameSize = 1
                    "Two" -> gameManager.gameSize = 2
                    "Three" -> gameManager.gameSize = 3
                    "Four" -> gameManager.gameSize = 4
                    "Five" -> gameManager.gameSize = 5
                    "Six" -> gameManager.gameSize = 6
                    "Seven" -> gameManager.gameSize = 7
                    "Eight" -> gameManager.gameSize = 8
                    "Nine" -> gameManager.gameSize = 9
                    "Ten" -> gameManager.gameSize = 10
                }
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        if (parent.id == R.id.difficulty_spinner) {
            difficulty = R.string.GameSetup_easy.toString()
        } else if (parent.id == R.id.size_spinner) {
            gameSize = R.string.one.toString()
        }
    }

    /**
     * Start the game based on the chosen mode
     */
    private fun proceedGame() {

        when (AppPreferences.getStr(getString(R.string.preferences_game_mode))) {
            "local_buzzer" -> { switchToGameWithParameters(BuzzerSetupActivity()) }
            "local_typing" -> { switchToGameWithParameters(TypingGameActivity()) }
            "local_lyrics" -> { switchToGameWithParameters(LyricsBelongGameActivity()) }
            "online_buzzer" -> { switchToGameWithParameters(PartyRoomActivity()) }
        }
    }
}
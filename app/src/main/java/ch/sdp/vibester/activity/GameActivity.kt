package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Button
import android.widget.ProgressBar
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.GameManager

/**
 * Common set up for all games (difficulty level, progress bar)
 */
open class GameActivity : AppCompatActivity() {
    open val handler = Handler()
    open var maxTime: Int = 30
    var runnable: Runnable? = null

    fun setMax(intent: Intent) {
        if(intent.hasExtra("Difficulty")) {
            when(intent.extras?.getString("Difficulty", "Easy")) {
                "Easy" -> maxTime = 30
                "Medium" -> maxTime = 15
                "Hard" -> maxTime = 5
            }
        }
    }

    fun initializeBarTimer(myBar: ProgressBar) {
        myBar.max = maxTime
        myBar.progress = maxTime
        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.cg_blue))
    }

    fun decreaseBarTimer(myBar: ProgressBar) {
        if (myBar.progress == maxTime/2) {
            myBar.progressTintList =
                ColorStateList.valueOf(getColor(R.color.maximum_yellow_red))
        } else if (myBar.progress == 5) {
            myBar.progressTintList =
                ColorStateList.valueOf(getColor(R.color.light_coral))
        }
        myBar.progress -= 1
    }

    fun checkRunnable() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
    }

    fun switchToEnding(gameManager: GameManager) {
        val intent = Intent(this, GameEndingActivity::class.java)
        val incArray: ArrayList<String> = ArrayList(
            gameManager.getWrongSongs().map { it.getTrackName() + " - " + it.getArtistName() })

        val statNames: ArrayList<String> = arrayListOf()
        val statName = "Total Score"
        statNames.addAll(arrayOf(statName, statName, statName, statName, statName))

        val statVal: ArrayList<String> = arrayListOf()
        val score = gameManager.getScore().toString()
        statVal.addAll(arrayOf(score, score, score, score, score))

        intent.putExtra("nbIncorrectSong", gameManager.getWrongSongs().size)

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }

    fun toggleBtnVisibility(btnId: Int, value: Boolean){
        val btn = findViewById<Button>(btnId)
        if (value) {
            btn.visibility = android.view.View.VISIBLE
        } else {
            btn.visibility = android.view.View.GONE
        }
    }

    /**
     * Function called in the end of each round.
     */
    open fun endRound(gameManager: GameManager) {
        checkRunnable()
        if (endGame(gameManager)) {
            switchToEnding(gameManager)
        }
    }

    fun endGame(gameManager: GameManager): Boolean {
        return !gameManager.checkGameStatus() || !gameManager.setNextSong()
    }



}
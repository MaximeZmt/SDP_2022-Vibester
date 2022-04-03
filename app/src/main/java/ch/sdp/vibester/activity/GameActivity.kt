package ch.sdp.vibester.activity

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.ProgressBar
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.GameManager

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

        intent.putExtra("nbIncorrectSong", gameManager.gameSize - gameManager.getScore())

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }

    fun checkRunnable() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
    }


}
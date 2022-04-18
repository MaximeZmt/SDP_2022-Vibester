package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        if (isEndGame(gameManager)) {
            switchToEnding(gameManager)
        }
    }

    fun isEndGame(gameManager: GameManager): Boolean {
        return !gameManager.checkGameStatus() || !gameManager.setNextSong()
    }

    fun superTestProgressBar(myBar: ProgressBar, progressTime: Int=0){
        myBar.progress = progressTime
    }

    fun superTestProgressBarColor(myBar: ProgressBar): ColorStateList? {
        return myBar.progressTintList
    }

    companion object {
        /**
         * Generate spaces widget programmatically
         */
        fun generateSpace(width: Int, height: Int, ctx: Context): Space {
            val space = Space(ctx)
            space.minimumWidth = width
            space.minimumHeight = height
            return space
        }

        /**
         * Generate Text widget programmatically
         */
        fun generateText(txt: String, ctx: Context): TextView {
            val txtView = TextView(ctx)
            txtView.text = txt
            txtView.gravity = Gravity.CENTER
            txtView.minHeight = 200
            txtView.textSize = 20F
            txtView.setTextColor(ContextCompat.getColor(ctx, R.color.black))
            return txtView
        }

        /**
         * Generate an images widget programmatically given a song (retrieve song artwork asynchronously)
         */
        fun generateImage(song: Song, ctx: Context): ImageView {
            val imgView = ImageView(ctx)
            imgView.minimumWidth = 200
            imgView.minimumHeight = 200

            CoroutineScope(Dispatchers.Main).launch {
                val task = async(Dispatchers.IO) {
                    val bit = BitmapGetterApi.download(song.getArtworkUrl())
                    bit.get()
                }
                val bm = task.await()
                imgView.setImageBitmap(bm)
            }
            imgView.foregroundGravity = Gravity.LEFT
            return imgView
        }
    }



}
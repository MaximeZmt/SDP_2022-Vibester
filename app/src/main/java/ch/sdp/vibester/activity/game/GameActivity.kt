package ch.sdp.vibester.activity.game

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Handler
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.GameEndingActivity
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Song
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Common set up for all games (difficulty level, progress bar)
 * CHECK IF THE DOCS ARE CORRECT OR NOT!
 */
@AndroidEntryPoint
open class GameActivity : AppCompatActivity() {
    open val handler = Handler()
    open var maxTime: Int = 30
    var runnable: Runnable? = null

    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    /**
     * Sets the countdown timer's maximum(initial) value.
     */
    fun setMax(intent: Intent) {
        if(intent.hasExtra("Difficulty")) {
            when(intent.extras?.getString("Difficulty", "Easy")) {
                "Easy" -> maxTime = 30
                "Medium" -> maxTime = 15
                "Hard" -> maxTime = 5
            }
        }
    }

    /**
     * Initializes the progress bar, i.e the timer to static values, mostly read from intents.
     */
    fun initializeBarTimer(myBar: ProgressBar) {
        myBar.max = maxTime
        myBar.progress = maxTime
        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.cg_blue))
    }

    /**
     * Handles unit decrease of the timer of the progress bar, and the effects of this change.
     */
    fun decreaseBarTimer(myBar: ProgressBar) {
        if (myBar.progress == maxTime/2 && myBar.progress > 5) {
            myBar.progressTintList =
                ColorStateList.valueOf(getColor(R.color.maximum_yellow_red))
        } else if (myBar.progress == 5) {
            myBar.progressTintList =
                ColorStateList.valueOf(getColor(R.color.light_coral))
        }
        myBar.progress -= 1
    }

    /**
     * Custom handle of the bar progress.
     */
    fun barTimer(myBar: ProgressBar, ctx:Context, gameManager: GameManager, checkAnswer: (ctx: Context, chosenSong: Song?, gameManager: GameManager) -> Unit){
        initializeBarTimer(myBar)
        runnable = object : Runnable {
            override fun run() {
                if (myBar.progress > 0) {
                    decreaseBarTimer(myBar)
                    handler.postDelayed(this, 999) //just a bit shorter than a second for safety
                } else if (myBar.progress == 0) {
                    if (gameManager.playingMediaPlayer()) {
                        gameManager.stopMediaPlayer()
                    }
                    // chosen song is null because no answer has been given before the progress bar times out
                    checkAnswer(ctx, null, gameManager)
                }
            }
        }
        handler.post(runnable!!)
    }

    /**
     * Checks the state of the runnable and removes callbacks.
     */
    fun checkRunnable() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
    }

    /**
     * Custom onDestroy to verify progressbar is stopped
     */
    override fun onDestroy() {
        checkRunnable()
        super.onDestroy()
    }

    /**
     * Called upon the ending of a game. Passes gathered information during the game to the next
     * activity through the intent.
     */
    private fun switchToEnding(gameManager: GameManager) {
        val intent = Intent(this, GameEndingActivity::class.java)

        //Set list of incorrect songs
        val incorrectSongList: ArrayList<String> = createSongList(gameManager.getWrongSongs())
        intent.putStringArrayListExtra("incorrectSongList", incorrectSongList)

        //Set list of correct songs
        val correctSongList: ArrayList<String> = createSongList(gameManager.getCorrectSongs())
        intent.putStringArrayListExtra("correctSongList", correctSongList)

        // Set statistics
        val statNames: ArrayList<String> = arrayListOf()
        val statName = "Score"
        statNames.addAll(arrayOf(statName))

        val statVal: ArrayList<String> = arrayListOf()
        val score = gameManager.getScore().toString()
        statVal.addAll(arrayOf(score))

        intent.putStringArrayListExtra("statNames", statNames)
        intent.putStringArrayListExtra("statValues", statVal)

        startActivity(intent)
    }


    fun createSongList(songs: MutableList<Song>): ArrayList<String> {
        return ArrayList(songs.map { it.getTrackName() + " - " + it.getArtistName() })
    }

    /**
     * Function to set a song for the first round and play a game.
     */
    fun startFirstRound(ctx: Context, gameManager: GameManager, startRound: (ctx: Context, gameManager: GameManager) -> Unit){
        if (!isEndGame(gameManager)) {
            startRound(ctx, gameManager)
        }
        else {
            switchToEnding(gameManager)
        }
    }

    /**
     * Function called in the end of each round.
     * @param gameManager: the manager for the current game
     * @param callback: a unit function that is called if the game has reached its end
     */
    open fun endRound(gameManager: GameManager, callback: (()->Unit)?= null) {
        checkRunnable()
        if (isEndGame(gameManager)) {
            callback?.invoke()
            switchToEnding(gameManager)
        }
    }

    /**
     * Function to check if the game has ended or not.
     * @param gameManager: the manager for the current game
     */
    fun isEndGame(gameManager: GameManager): Boolean {
        return !gameManager.checkGameStatus() || !gameManager.setNextSong()
    }

    /**
     * Function to set scores in the end of the game
     * @param gameManager: the manager for the current game
     */
    fun setScores(gameManager: GameManager) {
        if(authenticator.isLoggedIn()){
            dataGetter.updateFieldInt(authenticator.getCurrUID(), "totalGames", 1, method = "sum")
            dataGetter.updateSubFieldInt(authenticator.getCurrUID(), gameManager.getScore(), "scores", gameManager.gameMode, method = "best")
        }
    }

    /**
     * Function used for testing. Do not call unless it is for that specific purpose.
     */
    fun superTestProgressBar(myBar: ProgressBar, progressTime: Int=0) {
        myBar.progress = progressTime
    }

    /**
     * Shows a variable score on a toast.
     * @param ctx
     * @param score: the score to show on the toast
     */
    fun toastShowCorrect(ctx: Context, score: Int) {
        Toast.makeText(ctx, ctx.getString(R.string.correct_message, score), Toast.LENGTH_SHORT).show()
    }

    /**
     * Shows the correct answer on a toast.
     * @param ctx
     * @param itWas: the correct song to display
     */
    fun toastShowWrong(ctx: Context, itWas: Song) {
        Toast.makeText(
            ctx,
            ctx.getString(R.string.wrong_message_with_answer, itWas.getTrackName(), itWas.getArtistName()),
            Toast.LENGTH_SHORT
        ).show()
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

    /**
     * Add a given song to a new layout in a particular manner, which is then returned.
     */
    fun showSongAndImage(song: Song, ctx: Context): LinearLayout {
        val linLay = LinearLayout(ctx)
        linLay.setHorizontalGravity(1)
        linLay.gravity = Gravity.LEFT

        linLay.addView(generateImage(song, ctx))
        linLay.addView(generateSpace(100, 100, ctx))
        linLay.addView(generateText(song.getArtistName() + " - " + song.getTrackName(), ctx))

        return linLay
    }
}
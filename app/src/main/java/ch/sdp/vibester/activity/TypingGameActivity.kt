package ch.sdp.vibester.activity

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import ch.sdp.vibester.R
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.DisplayContents
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.TypingGameManager
import ch.sdp.vibester.model.Song
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Class that represent a game
 */
@AndroidEntryPoint
class TypingGameActivity : GameActivity() {
    private lateinit var gameManager: TypingGameManager
    private var gameIsOn: Boolean = true // done to avoid clicks on songs after the round is over

    @Inject
    lateinit var dataGetter: DataGetter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_typing_game)

        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        val inputTxt = findViewById<EditText>(R.id.yourGuessET)
        val ctx: Context = this

        val getIntent = intent.extras
        if (getIntent != null) {
            super.setMax(intent)
            gameManager = getIntent.getSerializable("gameManager") as TypingGameManager
            setNextButtonListener(ctx, gameManager)
            startFirstRound(ctx, gameManager)
        }
        setGuessLayoutListener(inputTxt, guessLayout)
    }

    /**
     * Custom onDestroy to verify progressbar and media player are stopped
     */
    override fun onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
        if (this::gameManager.isInitialized && gameManager.initializeMediaPlayer()) {
            gameManager.stopMediaPlayer()
        }
        super.onDestroy()
    }


    /**
     * Listener to operate the guess layout.
     */
    private fun setGuessLayoutListener(inputTxt:EditText, guessLayout: LinearLayout){
        inputTxt.addTextChangedListener {
            guessLayout.removeAllViews()
            val txtInp = inputTxt.text.toString()
            if (txtInp.length > 3) {
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO) {
                        ItunesMusicApi.querySong(txtInp, OkHttpClient(), 3).get()
                    }
                    try {
                        val list = Song.listSong(task.await())
                        for (x: Song in list) {
                            guess(x, findViewById(R.id.displayGuess), this@TypingGameActivity, gameManager)
                        }
                    } catch (e: Exception) {
                        Log.e("Exception: ", e.toString())
                    }
                }
            }
        }
    }

    /**
     * Set and remove nextBtn during the game
     */
    private fun toggleNextBtnVisibility(value: Boolean){
        toggleBtnVisibility(R.id.nextSong, value)
    }

    /**
     * Set listener for nextButton. When pressed, new round will start.
     */
    private fun setNextButtonListener(ctx: Context, gameManager: TypingGameManager){
        findViewById<Button>(R.id.nextSong).setOnClickListener {
            startRound(ctx, gameManager)
        }
    }

    /**
     * Custom handle of the bar progress.
     */
    private fun barTimer(myBar: ProgressBar, ctx:Context, gameManager: TypingGameManager){
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
                    checkAnswer(ctx, null, gameManager)
                }
            }
        }
        handler.post(runnable!!)
    }

    /**
     * announce if the player won or not
     */
    private fun hasWon(ctx: Context, score: Int, hasWon: Boolean, itWas: Song) {
        if (hasWon) {
            toastShowCorrect(ctx, score)
        } else {
            Toast.makeText(
                ctx,
                ctx.getString(R.string.wrong_message_with_answer, itWas.getTrackName(), itWas.getArtistName()),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Generate a change of intent at the end of a game
     */
    fun checkAnswer(ctx: Context, chosenSong: Song?, gameManager: TypingGameManager) {
        val playedSong = gameManager.getCurrentSong()

        if (chosenSong != null && chosenSong.getTrackName() == playedSong.getTrackName() && chosenSong.getArtistName() == playedSong.getArtistName()) {
            gameManager.increaseScore()
            gameManager.addCorrectSong()
            hasWon(ctx, gameManager.getScore(), true, playedSong)
        } else {
            gameManager.addWrongSong()
            hasWon(ctx, gameManager.getScore(), false, playedSong)
        }
        endRound(gameManager)
    }

    /**
     * Create the frame layout and its logic of the suggestion when user is typing
     */
    fun guess(song: Song, guessLayout: LinearLayout, ctx: Context, gameManager: TypingGameManager): FrameLayout {
        val frameLay = FrameLayout(ctx)
        frameLay.background = DisplayContents.borderGen(ctx, R.color.maximum_yellow_red)

        frameLay.addView(showSongAndImage(song, ctx))
        guessLayout.addView(frameLay)

        //Create the Listener that is executed if we click on the frame layer
        frameLay.setOnClickListener {
            if (gameIsOn) {
                frameLay.setBackgroundColor(getColor(ctx, R.color.tiffany_blue))
                guessLayout.removeAllViews()
                guessLayout.addView(frameLay)
                if (gameManager.playingMediaPlayer()) {
                    gameManager.stopMediaPlayer()
                }
                checkAnswer(ctx, song, gameManager)
            }
        }

        guessLayout.addView(generateSpace(75, 75, ctx))
        return frameLay
    }


    /**
     * Function to set a song for the first round and play a game.
     */
    private fun startFirstRound(ctx: Context, gameManager: TypingGameManager){
        if (!isEndGame(gameManager)) {
            startRound(ctx, gameManager)
        }
        else{
            switchToEnding(gameManager)
        }
    }

    /**
     * Function called in the end of each round. Displays the button "Next" and
     * sets the next songs to play.
     */
    override fun endRound(gameManager: GameManager, callback: (() -> Unit)?) {
        gameIsOn = false
        findViewById<EditText>(R.id.yourGuessET).isEnabled = false
        super.endRound(gameManager,this::setScores)
        toggleNextBtnVisibility(true)
    }

    /**
     * Function to set a new round. It includes reinitializing activity elements,
     * and playing new song for the round.
     */
    private fun startRound(ctx: Context, gameManager: TypingGameManager) {
        gameIsOn = true
        findViewById<LinearLayout>(R.id.displayGuess).removeAllViews()
        findViewById<EditText>(R.id.yourGuessET).text.clear()
        findViewById<EditText>(R.id.yourGuessET).isEnabled = true
        toggleNextBtnVisibility(false)
        gameManager.playSong()
        checkRunnable()
        barTimer(findViewById(R.id.progressBarTyping), ctx, gameManager)
    }

    /**
     * Function to set scores in the end of the game
     */
    private fun setScores() {
        if(::gameManager.isInitialized && FireBaseAuthenticator.isLoggedIn()){
            dataGetter.updateFieldInt(FireBaseAuthenticator.getCurrentUID(), "totalGames", 1, method = "sum")
            dataGetter.updateFieldInt(FireBaseAuthenticator.getCurrentUID(), "correctSongs", gameManager.getCorrectSongs().size, method = "sum")
            dataGetter.updateFieldInt(FireBaseAuthenticator.getCurrentUID(), "bestScore", gameManager.getScore(), method = "best")
            dataGetter.updateSubFieldInt(FireBaseAuthenticator.getCurrentUID(), gameManager.getScore(), "scores", gameManager.gameMode, method = "best")
        }
    }
    /**
     * Functions for testing
     */
    fun testProgressBar(progressTime:Int = 0) {
        superTestProgressBar(findViewById(R.id.progressBarTyping), progressTime)
    }

    fun testFirstRound(ctx: Context, gameManager: TypingGameManager){
        startFirstRound(ctx, gameManager)
    }

    fun testProgressBarColor(): ColorStateList? {
        return superTestProgressBarColor(findViewById(R.id.progressBarTyping))
    }


}



package ch.sdp.vibester.activity.game

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import ch.sdp.vibester.R
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.helper.DisplayContents
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

/**
 * Class that represents a game
 */
class TypingGameActivity : GameActivity() {
    private lateinit var gameManager: GameManager
    private var gameIsOn: Boolean = true // done to avoid clicks on songs after the round is over
    private lateinit var nextBtn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_typing_game)

        nextBtn = findViewById(R.id.nextSongTyping)

        val ctx: Context = this
        val getIntent = intent.extras
        if (getIntent != null) {
            super.setMax(intent)
            gameManager = getIntent.getSerializable("gameManager") as GameManager
            setNextButtonListener(ctx, gameManager)
            super.startFirstRound(ctx, gameManager, ::startRoundTyping)
        }

        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        val inputTxt = findViewById<EditText>(R.id.yourGuessET)
        setGuessLayoutListener(inputTxt, guessLayout)

        val skipBtn = findViewById<Button>(R.id.skip_typing)
        skipBtn.setOnClickListener { checkAnswer(ctx, null, gameManager) }

    }

    /**
     * Custom onDestroy to verify progressbar and media player are stopped
     */
    override fun onDestroy() {
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
     * Set listener for nextButton. When pressed, new round will start.
     */
    private fun setNextButtonListener(ctx: Context, gameManager: GameManager){
        nextBtn.setOnClickListener {
            startRoundTyping(ctx, gameManager)
        }
    }

    /**
     * announce if the player won or not
     */
    private fun hasWon(ctx: Context, hasWon: Boolean, itWas: Song, gameManager: GameManager) {
        if (hasWon) {
            toastShowCorrect(ctx, gameManager.getCorrectSongs().size)
        } else {
            toastShowWrong(ctx, itWas)
        }
    }

    /**
     * Generate a change of intent at the end of a game
     */
    fun checkAnswer(ctx: Context, chosenSong: Song?, gameManager: GameManager) {
        val playedSong = gameManager.getCurrentSong()
        if (checkSong(chosenSong, playedSong)) {
            gameManager.addCorrectSong()
            hasWon(ctx, true, playedSong, gameManager)
        } else {
            gameManager.addWrongSong()
            hasWon(ctx, false, playedSong, gameManager)
        }
        if (gameManager.initializeMediaPlayer() && gameManager.playingMediaPlayer()) {
            gameManager.stopMediaPlayer()
        }
        endRound(gameManager)
    }

    /**
     * Create the frame layout and its logic of the suggestion when user is typing
     */
    fun guess(song: Song, guessLayout: LinearLayout, ctx: Context, gameManager: GameManager): FrameLayout {
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
                checkAnswer(ctx, song, gameManager)
            }
        }

        guessLayout.addView(generateSpace(75, 75, ctx))
        return frameLay
    }

    /**
     * Function called in the end of each round. Displays the button "Next" and
     * sets the next songs to play.
     */
    override fun endRound(gameManager: GameManager, callback: (() -> Unit)?) {
        gameIsOn = false
        findViewById<EditText>(R.id.yourGuessET).isEnabled = false
        super.endRound(gameManager, this::setScores)
        Helper().showBtn(nextBtn)
    }

    /**
     * Function to set a new round. It includes reinitializing activity elements,
     * and playing new song for the round.
     */
    private fun startRoundTyping(ctx: Context, gameManager: GameManager) {
        gameIsOn = true
        findViewById<LinearLayout>(R.id.displayGuess).removeAllViews()
        findViewById<EditText>(R.id.yourGuessET).text.clear()
        findViewById<EditText>(R.id.yourGuessET).isEnabled = true
        val roundText = ctx.getString(R.string.TypingGame_currentRound) + gameManager.getPlayedSongsCount().toString() + " / " + gameManager.gameSize.toString()
        val scoreText = ctx.getString(R.string.TypingGame_yourScore) + gameManager.getScore().toString()
        findViewById<TextView>(R.id.playerScore).text = roundText + "\n" + scoreText
        Helper().hideBtn(nextBtn)
        gameManager.playSong()
        checkRunnable()
        super.barTimer(findViewById(R.id.progressBarTyping), ctx, gameManager, ::checkAnswer)
    }

    /**
     * Function to set scores in the end of the game
     */
    private fun setScores() {
        if (::gameManager.isInitialized) {
            super.setScores(gameManager)
        }
    }

    /**
     * Checks if a song chosen by the player matches the played song
     * @param chosen: the song chosen by the player
     * @param played: the song currently played
     * @return a boolean indicating whether the two songs match
     */
    private fun checkSong(chosen: Song?, played: Song): Boolean {
        return chosen != null && chosen.getTrackName() == played.getTrackName() && chosen.getArtistName() == played.getArtistName()
    }

    /*
     * The following functions are helper for testing
     */
    fun testProgressBar(progressTime:Int = 0) {
        val bar = findViewById<ProgressBar>(R.id.progressBarTyping)
        superTestProgressBar(bar, progressTime)
    }

    fun testFirstRound(ctx: Context, gameManager: GameManager){
        startFirstRound(ctx, gameManager, ::startRoundTyping)
    }

    fun testProgressBarColor(): ColorStateList? {
        return findViewById<ProgressBar>(R.id.progressBarTyping).progressTintList
    }


}



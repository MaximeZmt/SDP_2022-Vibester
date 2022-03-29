package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import ch.sdp.vibester.EndBasicGameTemporary
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.CompletableFuture

/**
 * Class that represent a game
 */

class TypingGameActivity : AppCompatActivity() {
    private val h = Handler()
    private  var runnable: Runnable? = null
    private lateinit var gameManager: GameManager
        companion object {
            /**
             * Generate the border for a box
             */
            fun borderGen(ctx: Context): GradientDrawable {
                val border = GradientDrawable()
                border.setColor(getColor(ctx, R.color.maximum_yellow_red)) //white background
                border.setStroke(1, -0x1000000)
                return border
            }

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
                txtView.setText(txt)
                txtView.gravity = Gravity.CENTER
                txtView.minHeight = 200
                txtView.textSize = 20F
                txtView.setTextColor(getColor(ctx, R.color.black))
                return txtView
            }

            /**
             * Generate an images widget programmatically given a song (retrieve song artwork asychronously)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_typing_game)

        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        val inputTxt = findViewById<EditText>(R.id.yourGuessET)

        var mysong: Song? = null
        var mediaPlayer: CompletableFuture<MediaPlayer>? = null
        val ctx: Context = this
        val h = Handler()

        val getIntent = intent.extras
        if(getIntent != null){
            gameManager = getIntent.getSerializable("gameManager") as GameManager
            playRound(ctx, gameManager)
        }

        //Listener when we modify the input
        inputTxt.addTextChangedListener{
            guessLayout.removeAllViews()
            val txtInp = inputTxt.text.toString()
            if (txtInp.length>3){
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO){
                        ItunesMusicApi.querySong(txtInp, OkHttpClient(), 3).get()
                    }
                    try {
                        val list = Song.listSong(task.await())
                        for (x: Song in list) {
                            guess(x, findViewById(R.id.displayGuess),this@TypingGameActivity, gameManager!!)
                        }
                    } catch (e: Exception){
                        Log.e("Exception: ", e.toString())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if(runnable !=null ){
            h.removeCallbacks(runnable!!);}
        if(this::gameManager.isInitialized){
            gameManager.stopMediaPlayer()
        }
        super.onDestroy()
    }

    /**
     * announce if the player won or not
     */
    private fun hasWon(ctx: Context, score:Int, hasWon: Boolean, itwas: Song){
        if(hasWon){
            Toast.makeText(ctx,score.toString()+" Well Done!",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(ctx,"Sadly you're wrong, it was: "+itwas.getTrackName()+" by "+itwas.getArtistName(),Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Generate a change of intent at the end of the game
     */
    fun checkAnswer(ctx: Context, choosenSong: Song?, gameManager: GameManager){
        val playedSong = gameManager.getCurrentSong()

        if(choosenSong != null && choosenSong.getTrackName() == playedSong.getTrackName() && choosenSong.getArtistName() == playedSong.getArtistName()){
            gameManager.increaseScore()
            gameManager.addCorrectSong()
            hasWon(ctx, gameManager.getScore(),true,playedSong)
        }else{
            hasWon(ctx, gameManager.getScore(),false, playedSong)
        }
        playRound(ctx, gameManager)
    }

    /**
     * Create the frame layout and its logic of the suggestion when user is typing
     */
    fun guess(song: Song, guessLayout: LinearLayout, ctx: Context, gameManager: GameManager): FrameLayout{
        val frameLay = FrameLayout(ctx)
        frameLay.background = borderGen(ctx)

        // Horizontal Linear Layout to put Images and Text next one another
        val linLay = LinearLayout(ctx)
        linLay.setHorizontalGravity(1)
        linLay.gravity = Gravity.LEFT

        //Generate and add its component
        linLay.addView(generateImage(song, ctx))
        linLay.addView(generateSpace(100,100, ctx))
        linLay.addView(generateText(song.getArtistName() + " - " + song.getTrackName(), ctx))

        frameLay.addView(linLay)
        guessLayout.addView(frameLay)

        //Create the Listener that is executed if we click on the framelayer
        frameLay.setOnClickListener {
            frameLay.setBackgroundColor(getColor(ctx, R.color.tiffany_blue))
            guessLayout.removeAllViews()
            guessLayout.addView(frameLay)
            if (gameManager.playingMediaPlayer()) {
                gameManager.stopMediaPlayer()
            }
            checkAnswer(ctx, song, gameManager)
        }

        guessLayout.addView(generateSpace(75,75, ctx))
        return frameLay
    }

    /**
     * Custom handle of the bar progress.
     */
    private fun barTimer(myBar: ProgressBar, ctx:Context, gameManager: GameManager){
        myBar.progress = 30
        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.cg_blue))
        runnable = object : Runnable {
            override fun run() {
                if(myBar.progress>0){
                    if(myBar.progress == 15){
                        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.maximum_yellow_red))
                    }else if(myBar.progress == 5){
                        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.light_coral))
                    }
                    myBar.progress -= 1
                    h.postDelayed(this, 999) //just a bit shorter than a second for safety
                }else if(myBar.progress==0){
                    if(gameManager.playingMediaPlayer()){
                        gameManager.stopMediaPlayer()
                    }
                    checkAnswer(ctx, null, gameManager)
                }
            }
        }
        h.post(runnable!!)

    }

    /**
     * Function to set a new round. It includes reinitializing activity elements,
     * and setting new song for the round.
     */

     fun playRound(ctx: Context, gameManager: GameManager){
        if(gameManager.checkGameStatus() && gameManager.setNextSong()){
            findViewById<LinearLayout>(R.id.displayGuess).removeAllViews()
            findViewById<EditText>(R.id.yourGuessET).text.clear()
            gameManager.playSong()
            if(runnable !=null ){
                h.removeCallbacks(runnable!!);}
            barTimer(findViewById<ProgressBar>(R.id.progressBar), ctx, gameManager)}
        else{
            if(runnable !=null ){
                h.removeCallbacks(runnable!!);}
            val i = Intent(this, EndBasicGameTemporary::class.java)
            i.putExtra("score", gameManager.getScore().toString())
            startActivity(i)
        }
    }

    private fun switchToEnding(view: View,gameManager:GameManager) {
        val intent = Intent(this, GameEndingActivity::class.java)
        //MOCK VALUES FOR INCORRECT SONGS, ADAPT FROM GAME DATA IN THE FUTURE
        val incArray: ArrayList<String> = arrayListOf()
        incArray.addAll(arrayOf("One", "Two", "Three"))

        val statNames: ArrayList<String> = arrayListOf()
        statNames.addAll(arrayOf("Total Score"))

        val statVal: ArrayList<String> = arrayListOf()
        statVal.addAll(arrayOf(gameManager.getScore().toString()))

        intent.putExtra("nbIncorrectSong", gameManager.GAME_SIZE - gameManager.getScore())

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }
}
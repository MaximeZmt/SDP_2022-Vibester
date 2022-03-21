package ch.sdp.vibester.games

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import ch.sdp.vibester.R
import ch.sdp.vibester.api.AudioPlayer
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Class that represent a game
 */
class TypingGame : AppCompatActivity() {
    private lateinit var gameManager:GameManager
    companion object{

        /**
         * Print Toast message to announce the user if he wons or not
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
        fun intentGen(ctx: Context, choosenSong: Song?, playedSong: Song, gameManager: GameManager):Intent{

            if(choosenSong != null && choosenSong.getTrackName() == playedSong.getTrackName() && choosenSong.getArtistName() == playedSong.getArtistName()){
                gameManager.increaseScore()
                hasWon(ctx, gameManager.getScore(),true,playedSong)
            }else{
                hasWon(ctx, gameManager.getScore(),false, playedSong)
            }

            val newIntent = Intent(ctx, TypingGame::class.java)
            newIntent.putExtra("song", gameManager.nextSong())
            newIntent.putExtra("isPlaying", false)
            newIntent.putExtra("gameManager", gameManager)
            newIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
            return newIntent
        }

        /**
         * Create the frame layout and its logic of the suggestion when user is typing
         */
        fun guess(song: Song, guessLayout: LinearLayout, ctx: Context, playedSong: Song, player: CompletableFuture<MediaPlayer>?, gameManager: GameManager): FrameLayout{
            val frameLay = FrameLayout(ctx)
            frameLay.background = borderGen()

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
                frameLay.setBackgroundColor(getColor(ctx, R.color.teal_200))
                guessLayout.removeAllViews()
                guessLayout.addView(frameLay)
                val playerMedia = player?.get()
                if (playerMedia != null) {
                    playerMedia.stop()
                }
                startActivity(ctx, intentGen(ctx, song, playedSong, gameManager), null)
            }

            guessLayout.addView(generateSpace(75,75, ctx))
            return frameLay
        }


        /**
         * Generate the border for a box
         */
        fun borderGen(): GradientDrawable{
            val border = GradientDrawable()
            border.setColor(-0x1) //white background
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
            txtView.minHeight= 200
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
                val task = async(Dispatchers.IO){
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
        setContentView(R.layout.activity_typing_game)

        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        val inputTxt = findViewById<EditText>(R.id.yourGuessET)
        val myBar = findViewById<ProgressBar>(R.id.progressBar)

        var mysong: Song? = null
        var mediaPlayer: CompletableFuture<MediaPlayer>? = null
        val ctx: Context = this

        myBar.progress = 30

        val getIntent = intent.extras
        if(getIntent != null){
            val playableSong: Song = getIntent.get("song") as Song
//            val isPlaying: Boolean = getIntent.get("isPlaying") as Boolean
//            val hasWon: Boolean = getIntent.get("hasWon") as Boolean
            gameManager = getIntent.getSerializable("gameManager") as GameManager

//            if(!isPlaying){
//                //Is the activity showing the result
//                hasWon(this, hasWon, playableSong)
//                inputTxt.setKeyListener(null)
//            }else{
                //Is the activity playing music
                mediaPlayer = AudioPlayer.playAudio(playableSong.getPreviewUrl())
                val h = Handler()
                h.post(object : Runnable {
                    override fun run() {
                        if(myBar.progress>0){
                            myBar.progress -= 1
                            h.postDelayed(this, 999) //just a bit shorter than a second for safety
                        }else if (myBar.progress==0){
                            if(mysong != null){
                                if(mediaPlayer.get().isPlaying){
                                    startActivity(intentGen(ctx, null, playableSong, gameManager))
                                    finish()
                                }
                            }
                        }
                    }
                })

            mysong = playableSong
        }

        //Listener when we modify the input
        inputTxt.addTextChangedListener{
            guessLayout.removeAllViews()
            val txtInp = inputTxt.text.toString()
            if (txtInp.length>4){
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO){
                        ItunesMusicApi.querySong(txtInp, OkHttpClient(), 3).get()
                    }
                    val list = Song.listSong(task.await())
                    for(x: Song in list){
                        if (mysong != null) {
                            guess(x, findViewById(R.id.displayGuess), this@TypingGame, mysong, mediaPlayer, gameManager)
                        }else{
                            guess(x, findViewById(R.id.displayGuess), this@TypingGame, x, mediaPlayer, gameManager)
                        }
                    }
                }
            }
        }



    }

}
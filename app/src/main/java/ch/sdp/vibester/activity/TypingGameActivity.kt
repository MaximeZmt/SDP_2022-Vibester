package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import java.util.concurrent.CompletableFuture

/**
 * Class that represent a game
 */
class TypingGameActivity : AppCompatActivity() {

    companion object{

        /**
         * Print Toast message to announce the user if he wons or not
         */
        private fun hasWon(ctx: Context, hasWon: Boolean, itWas: Song){
            if(hasWon){
                Toast.makeText(ctx,"Well Done!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(ctx,"Sadly you're wrong, it was: "+itWas.getTrackName()+" by "+itWas.getArtistName(),Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * Generate a change of intent at the end of the game
         */
        fun intentGen(ctx: Context, chosenSong: Song?, playedSong: Song):Intent{
            val newIntent = Intent(ctx, TypingGameActivity::class.java)
            newIntent.putExtra("song", playedSong)
            newIntent.putExtra("isPlaying", false)
            if(chosenSong != null && chosenSong.getTrackName() == playedSong.getTrackName() && chosenSong.getArtistName() == playedSong.getArtistName()){
                newIntent.putExtra("hasWon", true)
            }else{
                newIntent.putExtra("hasWon", false)
            }
            newIntent.flags = FLAG_ACTIVITY_NEW_TASK
            return newIntent
        }

        /**
         * Create the frame layout and its logic of the suggestion when user is typing
         */
        fun guess(song: Song, guessLayout: LinearLayout, ctx: Context, playedSong: Song, player: CompletableFuture<MediaPlayer>?): FrameLayout{
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

            //Create the Listener that is executed if we click on the frameLayer
            frameLay.setOnClickListener {
                frameLay.setBackgroundColor(getColor(ctx, R.color.tiffany_blue))
                guessLayout.removeAllViews()
                guessLayout.addView(frameLay)
                val playerMedia = player?.get()
                if (playerMedia != null) {
                    playerMedia.stop()
                }
                startActivity(ctx, intentGen(ctx, song, playedSong), null)
            }

            guessLayout.addView(generateSpace(75,75, ctx))
            return frameLay
        }


        /**
         * Generate the border for a box
         */
        fun borderGen(ctx: Context): GradientDrawable{
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
            txtView.text = txt
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_typing_game)

        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        val inputTxt = findViewById<EditText>(R.id.yourGuessET)

        var mysong: Song? = null
        var mediaPlayer: CompletableFuture<MediaPlayer>? = null
        val ctx: Context = this



        val getIntent = intent.extras
        if(getIntent != null){
            val playableSong: Song = getIntent.get("song") as Song
            val isPlaying: Boolean = getIntent.get("isPlaying") as Boolean
            val hasWon: Boolean = getIntent.get("hasWon") as Boolean

            if(!isPlaying){
                //Is the activity showing the result
                hasWon(this, hasWon, playableSong)
                inputTxt.keyListener = null
            }else{
                //Is the activity playing music
                mediaPlayer = AudioPlayer.playAudio(playableSong.getPreviewUrl())
                barTimer(findViewById<ProgressBar>(R.id.progressBar), mediaPlayer, mysong, ctx)
            }
            mysong = playableSong
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
                            if (mysong != null) {
                                guess(x, findViewById(R.id.displayGuess),this@TypingGameActivity, mysong, mediaPlayer)
                            }
                        }
                    } catch (e: Exception){
                        Log.e("Exception: ", e.toString())
                    }
                }
            }
        }
    }


    fun barTimer(myBar: ProgressBar, mediaPlayer: CompletableFuture<MediaPlayer>, mySong: Song?, ctx:Context){
        myBar.progress = 30
        val h = Handler()
        h.post(object : Runnable {
            override fun run() {
                if(myBar.progress>0){
                    if(myBar.progress == 15){
                        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.maximum_yellow_red))
                    }else if(myBar.progress == 5){
                        myBar.progressTintList = ColorStateList.valueOf(getColor(R.color.light_coral))
                    }
                    myBar.progress -= 1
                    h.postDelayed(this, 999) //just a bit shorter than a second for safety
                }else if (myBar.progress==0){
                    if(mySong != null){
                        if(mediaPlayer.get().isPlaying){
                            //TODO Intent Switch for end
                            finish()
                        }
                    }
                }
            }
        })
    }


}
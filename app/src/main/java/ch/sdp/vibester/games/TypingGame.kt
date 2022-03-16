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
import java.util.concurrent.CompletableFuture


class TypingGame : AppCompatActivity() {

    companion object{

        private fun hasWon(ctx: Context, hasWon: Boolean, itwas: Song){
            if(hasWon){
                Toast.makeText(ctx,"Well Done!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(ctx,"Sadly you're wrong, it was: "+itwas.getTrackName()+" by "+itwas.getArtistName(),Toast.LENGTH_SHORT).show()
            }
        }

        fun intentGen(ctx: Context, choosenSong: Song?, playedSong: Song):Intent{
            val newIntent = Intent(ctx, TypingGame::class.java)
            newIntent.putExtra("song", playedSong)
            newIntent.putExtra("isPlaying", false)
            if(choosenSong != null && choosenSong.getTrackName() == playedSong.getTrackName() && choosenSong.getArtistName() == playedSong.getArtistName()){
                newIntent.putExtra("hasWon", true)
            }else{
                newIntent.putExtra("hasWon", false)
            }
            newIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
            return newIntent
        }

        fun guess(song: Song, guessLayout: LinearLayout, ctx: Context, playedSong: Song, player: CompletableFuture<MediaPlayer>?, acti: Activity): FrameLayout{
            val frameLay = FrameLayout(ctx)
            frameLay.background = borderGen()

            val linLay = LinearLayout(ctx)

            linLay.setHorizontalGravity(1)
            linLay.gravity = Gravity.LEFT
            linLay.addView(generateImage(song, ctx))
            linLay.addView(generateSpace(100,100, ctx))
            linLay.addView(generateText(song.getArtistName() + " - " + song.getTrackName(), ctx))

            frameLay.addView(linLay)
            guessLayout.addView(frameLay)

            frameLay.setOnClickListener {
                frameLay.setBackgroundColor(getColor(ctx, R.color.teal_200))
                guessLayout.removeAllViews()
                guessLayout.addView(frameLay)
                val playerMedia = player?.get()
                if (playerMedia != null) {
                    playerMedia.stop()
                }
                startActivity(ctx, intentGen(ctx, song, playedSong), null)
                ActivityCompat.finishAffinity(acti)
            }

            guessLayout.addView(generateSpace(75,75, ctx))
            return frameLay
        }


        fun borderGen(): GradientDrawable{
            val border = GradientDrawable()
            border.setColor(-0x1) //white background
            border.setStroke(1, -0x1000000)
            return border
        }

        fun generateSpace(width: Int, height: Int, ctx: Context): Space {
            val space = Space(ctx)
            space.minimumWidth = width
            space.minimumHeight = height
            return space
        }

        fun generateText(txt: String, ctx: Context): TextView {
            val txtView = TextView(ctx)
            txtView.setText(txt)
            txtView.gravity = Gravity.CENTER
            txtView.minHeight= 200
            txtView.textSize = 20F
            txtView.setTextColor(getColor(ctx, R.color.black))
            return txtView
        }


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

        val getIntent = intent.extras

        var mysong: Song? = null
        var mediaPlayer: CompletableFuture<MediaPlayer>? = null

        val ctx: Context = this as Context

        myBar.progress = 30
        val myActi = this as Activity


        if(getIntent != null){
            val playableSong: Song = getIntent.get("song") as Song
            val isPlaying: Boolean = getIntent.get("isPlaying") as Boolean
            val hasWon: Boolean = getIntent.get("hasWon") as Boolean

            if(!isPlaying){
                TypingGame.hasWon(this, hasWon, playableSong)
                inputTxt.setKeyListener(null)
            }else{
                mediaPlayer = AudioPlayer.playAudio(playableSong.getPreviewUrl())
                val h = Handler()
                h.post(object : Runnable {
                    override fun run() {
                        if(myBar.progress>0){
                            myBar.progress -= 1
                            h.postDelayed(this, 1000)
                        }else if (myBar.progress==0){
                            if(mysong != null){
                                startActivity(TypingGame.intentGen(ctx, null, playableSong))
                                finish()
                            }
                        }
                    }
                })
            }
            mysong = playableSong
        }



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
                            guess(x, findViewById<LinearLayout>(R.id.displayGuess), this@TypingGame, mysong, mediaPlayer, myActi)
                        }else{
                            guess(x, findViewById<LinearLayout>(R.id.displayGuess), this@TypingGame, x, mediaPlayer, myActi)
                        }
                    }
                }
            }
        }


    }

}
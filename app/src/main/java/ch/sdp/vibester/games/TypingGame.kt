package ch.sdp.vibester.games

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


class TypingGame : AppCompatActivity() {

    companion object{

        fun guess(song: Song, guessLayout: LinearLayout, ctx: Context): FrameLayout{
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

                val newIntent = Intent(ctx, TypingGame::class.java)
                newIntent.putExtra("song", song)
                startActivity(ctx, newIntent, null)
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
                        guess(x, findViewById<LinearLayout>(R.id.displayGuess), this@TypingGame)
                    }
                }
            }
        }


    }

}
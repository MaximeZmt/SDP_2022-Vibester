package ch.sdp.vibester.games

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_typing_game)


        val inputTxt = findViewById<EditText>(R.id.yourGuessET)

        inputTxt.addTextChangedListener{
            val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
            guessLayout.removeAllViews()
            val txtInp = inputTxt.text.toString()
            Log.e("yop", txtInp)
            if (txtInp.length>4){

                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO){
                        ItunesMusicApi.querySong(txtInp, OkHttpClient(), 3).get()
                    }
                    val list = Song.listSong(task.await())
                    for(x: Song in list){
                        guess(x)
                    }
                }
            }
        }


    }


    private fun guess(song: Song){
        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        val frameLay = FrameLayout(this)

        val border = GradientDrawable()
        border.setColor(-0x1) //white background
        border.setStroke(1, -0x1000000)
        frameLay.background = border

        val linLay = LinearLayout(this)

        val space = Space(this)
        space.minimumWidth =  100
        space.minimumHeight = 100

        linLay.setHorizontalGravity(1)
        linLay.gravity = Gravity.LEFT

        val imgView = ImageView(this)
        imgView.minimumWidth = 200
        imgView.minimumHeight = 200
        //imgView.maxHeight = 400
        //imgView.maxWidth = 400
        CoroutineScope(Dispatchers.Main).launch {
           val task = async(Dispatchers.IO){
               val bit = BitmapGetterApi.download(song.getArtworkUrl())
               bit.get()
            }
            val bm = task.await()
            imgView.setImageBitmap(bm)
        }

        imgView.foregroundGravity = Gravity.LEFT

        val txtView = TextView(this)
        txtView.setText(song.getArtistName() + " - " + song.getTrackName())
        txtView.gravity = Gravity.CENTER
        txtView.minHeight=200
        txtView.textSize = 20F
        txtView.setTextColor(getColor(R.color.black))


        linLay.addView(imgView)
        linLay.addView(space)
        linLay.addView(txtView)

        frameLay.addView(linLay)



        guessLayout.addView(frameLay)
        val space2 = Space(this)
        space2.minimumWidth = 75
        space2.minimumHeight = 75
        frameLay.setOnClickListener {
            Log.e("Test","yooooh")
            frameLay.setBackgroundColor(getColor(R.color.teal_200))
            guessLayout.removeAllViews()
            guessLayout.addView(frameLay)
        }

        guessLayout.addView(space2)

    }

}
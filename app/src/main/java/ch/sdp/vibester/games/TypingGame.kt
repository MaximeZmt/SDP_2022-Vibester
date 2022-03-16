package ch.sdp.vibester.games

import android.annotation.SuppressLint
import android.content.Intent
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
                        guess(x)
                    }
                }
            }
        }


    }

    private fun borderGen(): GradientDrawable{
        val border = GradientDrawable()
        border.setColor(-0x1) //white background
        border.setStroke(1, -0x1000000)
        return border
    }


    private fun guess(song: Song){
        val frameLay = FrameLayout(this)
        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)

        frameLay.background = borderGen()

        val linLay = LinearLayout(this)

        linLay.setHorizontalGravity(1)
        linLay.gravity = Gravity.LEFT
        linLay.addView(generateImage(song))
        linLay.addView(generateSpace(100,100))
        linLay.addView(generateText(song.getArtistName() + " - " + song.getTrackName()))

        frameLay.addView(linLay)
        guessLayout.addView(frameLay)

        frameLay.id = Int.MAX_VALUE


        frameLay.setOnClickListener {
            frameLay.setBackgroundColor(getColor(R.color.teal_200))
            guessLayout.removeAllViews()
            guessLayout.addView(frameLay)

            val newIntent = Intent(this, TypingGame::class.java)
            newIntent.putExtra("song", song)
            startActivity(newIntent)

        }

        guessLayout.addView(generateSpace(75,75))
    }

    private fun generateSpace(width: Int, height: Int): Space {
        val space = Space(this)
        space.minimumWidth = width
        space.minimumHeight = height
        return space
    }

    private fun generateText(txt: String): TextView {
        val txtView = TextView(this)
        txtView.setText(txt)
        txtView.gravity = Gravity.CENTER
        txtView.minHeight= 200
        txtView.textSize = 20F
        txtView.setTextColor(getColor(R.color.black))
        return txtView
    }


    private fun generateImage(song: Song): ImageView {
        val imgView = ImageView(this)
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
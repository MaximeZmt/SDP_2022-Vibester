package ch.sdp.vibester.games

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi


class TypingGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_typing_game)

        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)


/*
        val imgV = findViewById<ImageView>(R.id.imageView) as ImageView
        val bit = BitmapGetterApi.download("https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg")
        imgV.setImageBitmap(bit.get())

 */
        guess()
        guess()
        guess()
        guess()
        guess()
        guess()
        guess()
        guess()
    }


    private fun guess(){
        val frameLay = FrameLayout(this)

        frameLay.setOnClickListener { Log.e("Test","yooooh") }
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
        val bit = BitmapGetterApi.download("https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg")
        imgView.setImageBitmap(bit.get())
        imgView.foregroundGravity = Gravity.LEFT

        val txtView = TextView(this)
        txtView.setText("Imagine dragons - Mercury - My Life")
        txtView.gravity = Gravity.CENTER
        txtView.minHeight=200
        txtView.textSize = 20F
        txtView.setTextColor(getColor(R.color.black))


        linLay.addView(imgView)
        linLay.addView(space)
        linLay.addView(txtView)

        frameLay.addView(linLay)


        val guessLayout = findViewById<LinearLayout>(R.id.displayGuess)
        guessLayout.addView(frameLay)
        val space2 = Space(this)
        space2.minimumWidth = 75
        space2.minimumHeight = 75

        guessLayout.addView(space2)

    }

}
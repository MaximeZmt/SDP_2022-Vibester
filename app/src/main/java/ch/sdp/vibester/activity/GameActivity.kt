package ch.sdp.vibester.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import ch.sdp.vibester.helper.GameManager

open class GameActivity : AppCompatActivity() {
    open val h = Handler()
    open var maxTime: Int = 30
    //open lateinit var gameManager : GameManager

    fun setMax(intent: Intent) {
        if(intent.hasExtra("Difficulty")) {
            when(intent.extras?.getString("Difficulty", "Easy")) {
                "Easy" -> maxTime = 30
                "Medium" -> maxTime = 15
                "Hard" -> maxTime = 5
            }
        }
    }


}
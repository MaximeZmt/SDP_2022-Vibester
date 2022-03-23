package ch.sdp.vibester

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class EndBasicGameActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_basic_game)

        var bundle = intent.extras
        var txtView = findViewById<TextView>(R.id.score)
        if (bundle != null) {
            txtView.text = "Your score is "+ bundle.getString("score")
        }
    }
}
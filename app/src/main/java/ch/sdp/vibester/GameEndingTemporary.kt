package ch.sdp.vibester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class GameEndingTemporary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ending_temporary)

        var bundle = intent.extras
        var txtView = findViewById<TextView>(R.id.score)
        if (bundle != null) {
            txtView.text = "Your score is "+ bundle.getString("score")
        }
    }
}

package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtInput = findViewById<EditText>(R.id.mainNameInput)

        val btnGreeting = findViewById<Button>(R.id.mainButton)

        val gamescreenIntent = Intent(this, GamescreenActivity::class.java)

        btnGreeting.setOnClickListener {
            //gamescreenIntent.putExtra("name", txtInput.text.toString())
            startActivity(gamescreenIntent)
        }


    }
}
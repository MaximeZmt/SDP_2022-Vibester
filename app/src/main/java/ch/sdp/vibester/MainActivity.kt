package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.api.ItunesMusicApi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtInput = findViewById<EditText>(R.id.mainNameInput)

        val btnGreeting = findViewById<Button>(R.id.mainButton)
        val greetingIntent = Intent(this, GreetingActivity::class.java)





        btnGreeting.setOnClickListener {
            greetingIntent.putExtra("name", txtInput.text.toString())
            ItunesMusicApi.playFromQuery(txtInput.text.toString(), this)
            startActivity(greetingIntent)
        }
    }
}
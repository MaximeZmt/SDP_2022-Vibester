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

        val myapi = ItunesMusicApi()




        btnGreeting.setOnClickListener {
            greetingIntent.putExtra("name", txtInput.text.toString())
            //ID Monday
            //myapi.playAudio("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a")
            myapi.findUrlPreview(txtInput.text.toString(), this)
            startActivity(greetingIntent)
        }
    }
}
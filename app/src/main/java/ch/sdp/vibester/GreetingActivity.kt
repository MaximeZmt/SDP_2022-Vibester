package ch.sdp.vibester

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GreetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val textViewActivity = findViewById<TextView>(R.id.greetName)

        val getIntent = intent.extras
        val name: String = getIntent?.get("name") as String

        Log.d("NameGet: ", name)

        textViewActivity.text = "Hello $name!"

    }
}
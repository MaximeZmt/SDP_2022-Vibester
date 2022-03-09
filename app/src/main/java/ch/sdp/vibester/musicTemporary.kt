package ch.sdp.vibester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import ch.sdp.vibester.api.ItunesMusicApi

class musicTemporary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_temporary)

        val txtInput = findViewById<EditText>(R.id.musicName)

        val btnValidate = findViewById<Button>(R.id.validate)

        btnValidate.setOnClickListener({
            ItunesMusicApi.playFromQuery(txtInput.text.toString(), this)
        })




    }
}
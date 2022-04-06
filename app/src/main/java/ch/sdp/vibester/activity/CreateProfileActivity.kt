package ch.sdp.vibester.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import ch.sdp.vibester.R
import ch.sdp.vibester.database.UsersRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var usersRepo: UsersRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        var email = intent.getStringExtra("email").toString()
        var username = findViewById<EditText>(R.id.accountUsername)
        var handle = findViewById<EditText>(R.id.accountHandle)

        val btCreateAcc = findViewById<Button>(R.id.createButton)

        btCreateAcc.setOnClickListener {
            usersRepo.createUser(
                email,
                username.text.toString(),
                handle.text.toString(),
                this::startNewActivity)

            val newIntent = Intent(this, ProfileActivity::class.java)
            newIntent.putExtra("email", email)

            startActivity(newIntent)
        }
    }

    private fun startNewActivity(email: String) {
        val newIntent = Intent(this, ProfileActivity::class.java)
        newIntent.putExtra("email", email)

        startActivity(newIntent)
    }
}
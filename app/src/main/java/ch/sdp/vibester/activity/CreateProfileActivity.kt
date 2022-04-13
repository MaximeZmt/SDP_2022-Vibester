package ch.sdp.vibester.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.UsersRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var usersRepo: UsersRepo

    private val REQUEST_CODE = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        var email = intent.getStringExtra("email").toString()
        var username = findViewById<EditText>(R.id.accountUsername)
        var handle = findViewById<EditText>(R.id.accountHandle)

        val btCreateAcc = findViewById<Button>(R.id.createButton)
        val btnUploadImg = findViewById<Button>(R.id.uploadImg)


        btCreateAcc.setOnClickListener {
            usersRepo.createUser(
                email,
                username.text.toString(),
                handle.text.toString(),
                this::startNewActivity)
        }

        btnUploadImg.setOnClickListener {
            uploadImage()
        }
    }

    private fun startNewActivity(email: String) {
        val newIntent = Intent(this, ProfileActivity::class.java)
        newIntent.putExtra("email", email)

        startActivity(newIntent)
    }

    private fun uploadImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var image : ImageView = ImageView(this)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            image.setImageURI(data?.data)
            var bitmap = (image.drawable as BitmapDrawable).bitmap

        }
    }
}
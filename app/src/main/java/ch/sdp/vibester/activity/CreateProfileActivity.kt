package ch.sdp.vibester.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.ImageRepo
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var usersRepo: UsersRepo

    @Inject
    lateinit var imageRepo: ImageRepo

    private val REQUEST_CODE = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        val email = intent.getStringExtra("email").toString()
        val username = findViewById<EditText>(R.id.accountUsername)
        val handle = findViewById<EditText>(R.id.accountHandle)

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


    private fun updateUI() {
        findViewById<TextView>(R.id.uploadStatus).text = getString(R.string.uploadImageStatus)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imageRepo.uploadFile("profileImg/${Util.createNewId()}", data?.data!!) { updateUI() }
        }
    }
}
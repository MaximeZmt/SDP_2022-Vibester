package ch.sdp.vibester.activity.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var imageGetter: ImageGetter

    private val REQUEST_CODE = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        val email = intent.getStringExtra("email").toString()
        val username = findViewById<EditText>(R.id.accountUsername)

        val btCreateAcc = findViewById<Button>(R.id.createButton)
        val btnUploadImg = findViewById<Button>(R.id.uploadImg)

        btCreateAcc.setOnClickListener {
            dataGetter.setFieldValue(authenticator.getCurrUID(), "username", username.text.toString())
            startNewActivity(email)
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


    //check the UID here not sure
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imageGetter.uploadFile("profileImg/${authenticator.getCurrUID()}", data?.data!!) { updateUI() }
        }
    }
}
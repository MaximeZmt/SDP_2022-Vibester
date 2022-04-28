package ch.sdp.vibester.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var imageRepo: ImageRepo

    private val REQUEST_CODE = 500

    var isUnitTest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        isUnitTest = intent.getBooleanExtra("isUnitTest", false)

        val email = intent.getStringExtra("email").toString()
        val username = findViewById<EditText>(R.id.accountUsername)

        val btCreateAcc = findViewById<Button>(R.id.createButton)
        val btnUploadImg = findViewById<Button>(R.id.uploadImg)

        btCreateAcc.setOnClickListener {
            if (!TestMode.isTest()){
                dataGetter.updateFieldString(FireBaseAuthenticator.getCurrentUID(), username.text.toString(), "username")
            }
            startNewActivity(email)
        }

        btnUploadImg.setOnClickListener {
            uploadImage()
        }
    }

    private fun startNewActivity(email: String) {
        val newIntent = Intent(this, ProfileActivity::class.java)
        newIntent.putExtra("email", email)
        newIntent.putExtra("isUnitTest", isUnitTest)

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
            imageRepo.uploadFile("profileImg/${FireBaseAuthenticator.getCurrentUID()}", data?.data!!) { updateUI() }
        }
    }
}
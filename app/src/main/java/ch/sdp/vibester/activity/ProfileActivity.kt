package ch.sdp.vibester.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.user.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    @Inject
    lateinit var dataGetter: DataGetter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_profile)

        setEditUserNameBtnListener()
        setLogOutBtnListener()
        setRetToMainBtnListener()
        setShowQrCodeBtnListener()
        setQrCodeToProfileBtnListener()

        // Do not enable querying database while executing unit test
        val isUnitTest: Boolean = intent.getBooleanExtra("isUnitTest", false)

        if (!isUnitTest) {
            queryDatabase()
        } else {
            val upTest: User? = intent.getSerializableExtra("userTestProfile") as User?
            if (upTest == null) {
                setupProfile(User())
            } else {
                setupProfile(upTest)
            }
        }

    }

    private fun setEditUserNameBtnListener() {
        findViewById<Button>(R.id.editUser).setOnClickListener {
            showGeneralDialog(R.id.username, "username")
        }
    }

    private fun setLogOutBtnListener() {
        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            IntentSwitcher.switchBackToWelcome(this)
            finish()
        }
    }

    private fun setRetToMainBtnListener() {
        findViewById<FloatingActionButton>(R.id.profile_returnToMain).setOnClickListener {
            IntentSwitcher.switchBackToWelcome(this)
            finish()
        }
    }

    private fun setShowQrCodeBtnListener() {
        findViewById<Button>(R.id.showQRCode).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.profileContent).visibility = GONE
            findViewById<ConstraintLayout>(R.id.QrCodePage).visibility = VISIBLE
        }
    }

    private fun setQrCodeToProfileBtnListener() {
        findViewById<FloatingActionButton>(R.id.qrCode_returnToProfile).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.QrCodePage).visibility = GONE
            findViewById<ConstraintLayout>(R.id.profileContent).visibility = VISIBLE
        }
    }

    /**
     * A function that displays the dialog
     * @param title title of the dialog
     * @param hint hint of the text in the dialog
     * @param id id of the dialog
     * @param textId id of the text in the dialog
     * @param name of the dialog
     */

    private fun showDialog(title: String, hint: String, id: Int, textId: Int, name: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        val input = EditText(this)
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.id = id

        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            findViewById<TextView>(textId).text = input.text.toString()

            if(name == "username"){
                dataGetter.updateFieldString(FireBaseAuthenticator.getCurrentUID(), input.text.toString(), "username")
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    /**
     * A function that displays the dialog
     */
    private fun showGeneralDialog(id: Int, name: String) {
        val title = "Create $name"
        val hint = "Enter new $name"

        showDialog(title, hint, 0, id, name)
    }

    /**
     * A function that queries the database and fetched the correct user
     * Hard coded for now
     */
    private fun queryDatabase() {
        dataGetter.getUserData(this::setupProfile)
    }


    private fun setupProfile(user: User){

        // Currently assuming that empty username means no user !
        if (user.username != ""){
            findViewById<TextView>(R.id.username).text =  user.username
            findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
            findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
            findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
            findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        }
        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) {
                try {
                    Log.e(getString(R.string.log_tag),user.image)
                    val bit = BitmapGetterApi.download("https://raw.githubusercontent.com/Ashwinvalento/cartoon-avatar/master/lib/images/male/45.png")
                    bit.get(10, TimeUnit.SECONDS)
                }catch (e: Exception){
                    null
                }
            }
            val bm = task.await()

            if(bm != null){
                val avatar = findViewById<ImageView>(R.id.avatar)
                avatar.setImageBitmap(Bitmap.createScaledBitmap(bm, 1000,1000, false))
            }
        }

        generateQrCode(user.uid)
    }

    /**
     * generate the qr code bitmap of the given data
     */
    private fun generateQrCode(data: String) {
        val size = 512
        val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
        findViewById<ImageView>(R.id.qrCode).setImageBitmap(bmp)
    }
}


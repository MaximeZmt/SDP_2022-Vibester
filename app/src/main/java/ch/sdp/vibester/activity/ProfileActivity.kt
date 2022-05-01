package ch.sdp.vibester.activity

import android.graphics.*
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
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.glxn.qrgen.android.MatrixToImageWriter
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
            setLayoutVisibility(R.id.QrCodePage, true)
            setLayoutVisibility(R.id.profileContent, false)
        }
    }

    private fun setQrCodeToProfileBtnListener() {
        findViewById<FloatingActionButton>(R.id.qrCode_returnToProfile).setOnClickListener {
            setLayoutVisibility(R.id.QrCodePage, false)
            setLayoutVisibility(R.id.profileContent, true)
        }
    }

    private fun setLayoutVisibility(layout: Int, isVisible: Boolean){
        findViewById<ConstraintLayout>(layout).visibility = if (isVisible) VISIBLE else GONE
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


    private fun setupProfile(user: User) {
        // Currently assuming that empty username means no user !
        if (user.username != ""){
            findViewById<TextView>(R.id.username).text =  user.username
            findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
            findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
            findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
            findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        }
        setupProfilePhoto(user)

        if (user.uid != "") {
            generateQrCode(user.uid)
        }
    }

    private fun setupProfilePhoto(user: User) {
        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) {
                try {
                    Log.e(getString(R.string.log_tag),user.image)
                    val bit = BitmapGetterApi.download("https://raw.githubusercontent.com/Ashwinvalento/cartoon-avatar/master/lib/images/male/45.png")
                    bit.get(10, TimeUnit.SECONDS)
                } catch (e: Exception){
                    null
                }
            }
            val bm = task.await()
            if (bm != null) {
                val avatar = findViewById<ImageView>(R.id.avatar)
                avatar.setImageBitmap(Bitmap.createScaledBitmap(bm, 1000,1000, false))
            }
        }
    }

    /**
     * generate the qr code bitmap of the given data
     */
    private fun generateQrCode(data: String) {
        val size = 512
        val hints = HashMap<EncodeHintType?, Any?>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val bmp = MatrixToImageWriter.toBitmap(bits)
        val qrCodeCanvas = Canvas(bmp)

        val scaleFactor = 4 // resize the image
        val logo = BitmapFactory.decodeStream(assets.open("logo.png"))
        logo.density = logo.density * scaleFactor
        val xLogo = (size - logo.width / scaleFactor) / 2f
        val yLogo = (size - logo.height / scaleFactor) / 2f

        qrCodeCanvas.drawBitmap(logo, xLogo, yLogo, null)

        findViewById<ImageView>(R.id.qrCode).setImageBitmap(bmp)
    }
}


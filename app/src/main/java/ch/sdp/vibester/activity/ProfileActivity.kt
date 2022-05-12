package ch.sdp.vibester.activity

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.user.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
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

/**
 * Display user profile's data (image, username, scores, etc.) in UI
 */
@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    @Inject
    lateinit var imageGetter: ImageGetter

    val imageSize = 1000

    /**
     * Generic onCreate method belonging to ProfileActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile)

        setEditUserNameBtnListener()
        setLogOutBtnListener()
        setRetToMainBtnListener()
        setShowQrCodeBtnListener()
        setQrCodeToProfileBtnListener()
        setScoreBtnListener()

        queryDatabase()
    }

    /**
     * Generic listener for the edit username button.
     */
    private fun setEditUserNameBtnListener() {
        findViewById<ImageView>(R.id.editUser).setOnClickListener {
            showGeneralDialog(R.id.username, "username")
        }
    }

    /**
     * Generic listener for the log out button.
     */
    private fun setLogOutBtnListener() {
        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            IntentSwitcher.switch(this, MainActivity::class.java)
            finish()
        }
    }

    /**
     * Generic listener for the return to main button.
     */
    private fun setRetToMainBtnListener() {
        findViewById<FloatingActionButton>(R.id.profile_returnToMain).setOnClickListener {
            IntentSwitcher.switch(this, MainActivity::class.java)
            finish()
        }
    }

    /**
     * Generic listener for the show qr code button.
     */
    private fun setShowQrCodeBtnListener() {
        findViewById<ImageView>(R.id.showQRCode).setOnClickListener {
            setLayoutVisibility(findViewById<ConstraintLayout>(R.id.QrCodePage), true)
            setLayoutVisibility(findViewById<RelativeLayout>(R.id.profileContent), false)
        }
    }

    /**
     * Generic listener for the show qr code and return to profile button.
     */
    private fun setQrCodeToProfileBtnListener() {
        findViewById<FloatingActionButton>(R.id.qrCode_returnToProfile).setOnClickListener {
            setLayoutVisibility(findViewById<ConstraintLayout>(R.id.QrCodePage), false)
            setLayoutVisibility(findViewById<RelativeLayout>(R.id.profileContent), true)
        }
    }

    /**
     * Generic listener for the score button, show the score per genre statistic on click
     */
    private fun setScoreBtnListener() {
        findViewById<Button>(R.id.profile_scores).setOnClickListener {
            toggleVisibility(R.id.profileStatistics)
        }
    }

    /**
     * toggle the given layout's visibility
     * @param layout: The given ScrollView id to modify
     */
    private fun toggleVisibility(layout: Int) {
        if (findViewById<TableLayout>(layout).visibility == VISIBLE) findViewById<TableLayout>(layout).visibility = GONE
        else if (findViewById<TableLayout>(layout).visibility == GONE) findViewById<TableLayout>(layout).visibility = VISIBLE
    }

    /**
     * Sets the given layout's visibility.
     * @param layout: The given constraint layout to modify.
     * @param isVisible: The indicator of which visibility to choose.
     * True for VISIBLE, false for GONE.
     */
    private fun setLayoutVisibility(view: View, isVisible: Boolean){
        view.visibility = if (isVisible) VISIBLE else GONE
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
                dataGetter.setFieldValue(FireBaseAuthenticator().getCurrUID(), "username",  input.text.toString())
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    /**
     * A function that displays the dialog.
     * @param id: The id of the user to be shown.
     * @param name: The name of the user to be shown.
     */
    private fun showGeneralDialog(id: Int, name: String) {
        val title = "Create $name"
        val hint = "Enter new $name"

        showDialog(title, hint, 0, id, name)
    }

    /**
     * A function that queries the database and fetched the correct user.
     */
    private fun queryDatabase() {
        val currentUser = authenticator.getCurrUser()
        if(currentUser != null){
            dataGetter.getUserData(currentUser.uid, this::setupProfile)

        }
    }


    /**
     * A function that downloads an image and sets it.
     * @param imageURI URI of the image
     */
    private fun setImage(imageURI: Uri) {
        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) {
                try {
                    val bit = BitmapGetterApi.download(imageURI.toString())
                    bit.get(10, TimeUnit.SECONDS)
                } catch (e: Exception){

                    null
                }
            }
            val bm = task.await()

            if(bm != null){
                val avatar = findViewById<ImageView>(R.id.avatar)
                avatar.setImageBitmap(Bitmap.createScaledBitmap(bm, imageSize,imageSize, false))
            }
        }
    }


    /**
     * Function that sets the text of a given TextView to the string variant of an integer.
     * @param id: The id of the TextView to be changed.
     * @param text: The integer to be set as the text.
     */
    private fun setTextOfView(id: Int, text: Int) {
        findViewById<TextView>(id).text = text.toString()
    }

    /**
     * Function that sets the texts of multiple TextViews.
     * @param user: The user from which we will recover the text to set.
     */
    private fun setTextOfMultipleViews(user: User) {
        setTextOfView(R.id.profile_total_games_stat, user.totalGames)
        setTextOfView(R.id.profile_top_tracks, user.scores.getOrDefault("top tracks", 0))
        setTextOfView(R.id.profile_kpop, user.scores.getOrDefault("kpop", 0))
        setTextOfView(R.id.profile_rock, user.scores.getOrDefault("rock", 0))
        setTextOfView(R.id.profile_bts, user.scores.getOrDefault("BTS", 0))
        setTextOfView(R.id.profile_imagine_dragons, user.scores.getOrDefault("Imagine Dragons", 0))
        setTextOfView(R.id.profile_billie_eilish, user.scores.getOrDefault("Billie Eilish", 0))
    }

    /**
     * Function to handle setting up the profile.
     * @param user: The user whose profile we are setting up.
     */
    private fun setupProfile(user: User){
        // Currently assuming that empty username means no user !
        if (user.username != ""){
            findViewById<TextView>(R.id.username).text =  user.username
            setTextOfMultipleViews(user)
        }

        val imageID = dataGetter.getCurrentUser()?.uid
        imageGetter.fetchImage("profileImg/${imageID}", this::setImage)

        if (user.uid != "") {
            generateQrCode(user.uid)
        }
    }

    /**
     * generate the qr code bitmap of the given data
     * @param data Qr Code data
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

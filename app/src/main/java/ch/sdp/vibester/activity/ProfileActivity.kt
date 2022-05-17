package ch.sdp.vibester.activity

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.user.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
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
open class ProfileActivity @Inject constructor(val ctx: Context,
                                               val view: View,
                                               val imageGetter: ImageGetter,
                                               val dataGetter: DataGetter,
                                               val authenticator: FireBaseAuthenticator) {

    private val imageSize = 1000

    /**
     * Generic listener for the score button, show the score per genre statistic on click
     */
     fun setScoreBtnListener() {
        view.findViewById<Button>(R.id.profile_scores).setOnClickListener {
            toggleVisibility(R.id.profile_statistics)
        }
    }

    /**
     * toggle the given layout's visibility
     * @param layout: The given ScrollView id to modify
     */
    private fun toggleVisibility(layout: Int) {
        if (view.findViewById<TableLayout>(layout).visibility == VISIBLE) view.findViewById<TableLayout>(layout).visibility = GONE
        else if (view.findViewById<TableLayout>(layout).visibility == GONE) view.findViewById<TableLayout>(layout).visibility = VISIBLE
    }


    /**
     * A function that downloads an image and sets it.
     * @param imageURI URI of the image
     */
     fun setImage(imageURI: Uri) {
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

            if (bm != null) {
                val avatar = view.findViewById<ImageView>(R.id.avatar)
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
        view.findViewById<TextView>(id).text = text.toString()
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
    fun setupProfile(user: User){
        // Currently assuming that empty username means no user !
        if (user.username != "") {
            view. findViewById<TextView>(R.id.username).text =  user.username
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
        val logo = BitmapFactory.decodeStream(ctx.assets.open("logo.png"))
        logo.density = logo.density * scaleFactor
        val xLogo = (size - logo.width / scaleFactor) / 2f
        val yLogo = (size - logo.height / scaleFactor) / 2f

        qrCodeCanvas.drawBitmap(logo, xLogo, yLogo, null)

        view.findViewById<ImageView>(R.id.qrCode).setImageBitmap(bmp)
    }

    fun queryDatabase(uid: String = authenticator.getCurrUID()) {
         if(uid.isNotEmpty()) dataGetter.getUserData(uid, this::setupProfile)
    }
}


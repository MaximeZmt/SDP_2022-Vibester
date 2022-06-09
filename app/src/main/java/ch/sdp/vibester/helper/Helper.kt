package ch.sdp.vibester.helper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.MainActivity
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.concurrent.TimeUnit

class Helper {

    /**
     * Set the image of imageURI in avatar of given size
     * @param imageURI uri of the image
     * @param avatar ImageView
     * @param imageSize Int
     */
    fun setImage(imageURI: Uri, avatar: ImageView, imageSize: Int) {
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
                avatar.setImageBitmap(Bitmap.createScaledBitmap(bm, imageSize, imageSize, false))
            }
        }
    }

    /**
     * set up listener for return to main button
     * @param btn view of the button
     * @param ctx context
     */
    fun setReturnToMainListener(btn: View, ctx: Context) {
        btn.setOnClickListener {
            IntentSwitcher.switch(ctx, MainActivity::class.java)
        }
    }

    /**
     * @param uid uid of the player
     * @param scoresOrFollowing True goes scores False goes to following
     */
    fun goToPlayerProfileWithSection(uid: String, scoresOrFollowing: Boolean): Map<String, Serializable> {
        val section = if (scoresOrFollowing) R.string.profile_scores else R.string.profile_following
        return mapOf(Pair("UserId", uid), Pair("ScoresOrFollowing", section.toString()))
    }

    /**
     * Sets the visibility of the given button to VISIBLE if value is true, GONE otherwise.
     * @param btn: the Button view we want to change
     * @param value: true to make the button VISIBLE, false to make it GONE
     */
    private fun toggleBtnVisibility(btn: View, value: Boolean) {
        btn.visibility = if (value) VISIBLE else GONE
    }

    /**
     * hide the given button view
     */
    fun hideBtn(btn: View) {
        toggleBtnVisibility(btn, false)
    }

    /**
     * display the given button view
     */
    fun showBtn(btn: View) {
        toggleBtnVisibility(btn, true)
    }

    /**
     * Shows a variable score on a toast.
     * @param ctx
     * @param score: the score to show on the toast
     */
    fun toastShowCorrect(ctx: Context, score: Int) {
        Toast.makeText(ctx, ctx.getString(R.string.correct_message, score), Toast.LENGTH_SHORT).show()
    }

    /**
     * Shows the correct answer on a toast.
     * @param ctx
     * @param itWas: the correct song to display
     */
    fun toastShowWrong(ctx: Context, itWas: Song) {
        Toast.makeText(
            ctx,
            ctx.getString(R.string.wrong_message_with_answer, itWas.getTrackName(), itWas.getArtistName()),
            Toast.LENGTH_SHORT
        ).show()
    }

}
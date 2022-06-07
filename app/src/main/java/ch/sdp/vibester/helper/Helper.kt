package ch.sdp.vibester.helper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.MainActivity
import ch.sdp.vibester.api.BitmapGetterApi
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
}
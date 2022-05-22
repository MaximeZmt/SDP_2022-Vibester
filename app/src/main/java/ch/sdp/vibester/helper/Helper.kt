package ch.sdp.vibester.helper

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import ch.sdp.vibester.api.BitmapGetterApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
     * create an array list of the four given elements
     */
    fun createArrayOfFour(e1: Int, e2: Int, e3: Int, e4: Int) : ArrayList<Int> {
        return arrayListOf(e1, e2, e3, e4)
    }
}
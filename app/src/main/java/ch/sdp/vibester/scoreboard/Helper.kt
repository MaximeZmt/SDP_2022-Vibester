package ch.sdp.vibester.scoreboard

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Helper function to load image from given url
 */
fun ImageView.loadImg(url : String) {
    Glide.with(context).load(url).into(this)
}
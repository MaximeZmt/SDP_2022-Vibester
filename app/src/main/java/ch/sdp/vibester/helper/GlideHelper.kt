package ch.sdp.vibester.helper

import android.widget.ImageView
import ch.sdp.vibester.GlideApp

/**
 * Helper function to load image from given url
 */
fun ImageView.loadImg(url: String) {
    GlideApp.with(context).load(url).into(this)
}
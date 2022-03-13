package ch.sdp.vibester.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
    /**
     * Helper function to load image from given url into profile avatar
     */
    fun ImageView.loadImg(url: String) {
        Glide.with(context).load(url)
            .fitCenter()
            .circleCrop()
            .error(ColorDrawable(Color.RED))
            .placeholder(ColorDrawable(Color.GRAY))
            .into(this)
    }

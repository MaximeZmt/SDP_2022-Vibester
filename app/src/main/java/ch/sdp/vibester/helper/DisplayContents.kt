package ch.sdp.vibester.helper

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import ch.sdp.vibester.R

class DisplayContents private constructor (){
    companion object{
        /**
         * Generate the border for a box
         */
        fun borderGen(ctx: Context, color: Int): GradientDrawable {
            val border = GradientDrawable()
            border.setColor(ContextCompat.getColor(ctx, color)) //white background
            border.setStroke(1, -0x1000000)
            return border
        }
    }
}
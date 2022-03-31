package ch.sdp.vibester.helper

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.R
import org.junit.Assert.assertEquals
import org.junit.Test

class DisplayContentsTest {
    @Test
    fun borderGenTest() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val border = DisplayContents.borderGen(ctx, R.color.maximum_yellow_red)
        assertEquals(
            ContextCompat.getColor(ctx, R.color.maximum_yellow_red),
            border.color?.defaultColor
        )
    }

}
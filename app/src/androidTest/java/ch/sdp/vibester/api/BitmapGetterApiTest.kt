package ch.sdp.vibester.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.R
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


class BitmapGetterApiTest {

    // https://stackoverflow.com/questions/7619207/how-to-load-bitmap-from-res-without-resizing put images in drawable-nodpi
    @Test
    fun basicTest() {
        val bitmap =
            BitmapGetterApi.download("https://is4-ssl.mzstatic.com/image/thumb/Music114/v4/56/d3/71/56d37114-9b1d-3ddd-af77-7dae4389d2d2/source/30x30bb.jpg")
                .get()
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val bitmap2: Bitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.testbitmap)
        assertEquals(true, bitmap2.sameAs(bitmap))
    }


    @get:Rule
    var exception: ExpectedException = ExpectedException.none()

    @Test
    fun badUrlTest() {
        exception.expect(IllegalArgumentException::class.java)
        BitmapGetterApi.download("ThisIsSoMuchFun!").get()
    }

    @Test
    fun badUrlTest2() {
        exception.expect(Exception::class.java)
        BitmapGetterApi.download("https://ThisIsSoMuchFun!666.com").get()
    }

}
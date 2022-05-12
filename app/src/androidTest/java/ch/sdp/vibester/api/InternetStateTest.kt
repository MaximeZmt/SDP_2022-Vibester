package ch.sdp.vibester.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert.assertEquals
import org.junit.Test

class InternetStateTest {
    @Test
    fun internetStatus() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        assertEquals(true, InternetState.hasAccessedInternetOnce(ctx))
    }
}
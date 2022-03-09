package ch.sdp.vibester.api

import android.content.Context
import android.media.AudioManager
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.MainActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread


@RunWith(AndroidJUnit4::class)
class ItunesMusicApiTest{

    @get:Rule
    val testRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    /*
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.sdp.vibester", appContext.packageName)
        ItunesMusicApi.playFromQuery("imagine dragons believer", appContext)
        var manager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        assertEquals(true, manager!!.isMusicActive)
    }

     */



}
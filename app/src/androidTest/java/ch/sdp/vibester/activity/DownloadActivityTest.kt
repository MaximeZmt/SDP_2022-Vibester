package ch.sdp.vibester.activity

import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class DownloadActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(DownloadActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }


    @Test
    fun downloadCorrectSong() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadActivity::class.java)
        val scn: ActivityScenario<DownloadActivity> = ActivityScenario.launch(intent)
        val songName = "imagine dragons believer"

        onView(withId(R.id.download_songName)).perform(typeText(songName), closeSoftKeyboard())
        Thread.sleep(100)
        onView(withId(R.id.download_downloadsong)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.download_songName)).check(matches(withText("")))
        onView(withId(R.id.download_songName)).check(matches(withHint("Try another song!")))

        val extract = File("storage/emulated/0/Download", "extract_of_$songName")
        if(extract.exists()) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun downloadIncorrectSong() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadActivity::class.java)
        val scn: ActivityScenario<DownloadActivity> = ActivityScenario.launch(intent)
        val songName = "adsfasdgyasdfa"

        onView(withId(R.id.download_songName)).perform(typeText(songName), closeSoftKeyboard())
        Thread.sleep(100)
        onView(withId(R.id.download_downloadsong)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.download_songName)).check(matches(withText("")))
        onView(withId(R.id.download_songName)).check(matches(withHint("Please retry!")))

        val extract = File("storage/emulated/0/Download", "extract_of_$songName")
        if(extract.exists()) {
            assert(false)
        } else {
            assert(true)
        }
    }
}
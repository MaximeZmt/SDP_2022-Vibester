package ch.sdp.vibester.activity

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.FileInputStream

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

    private var waitForButton: Long = 100
    private var waitForDownload: Long = 1000

    @Test
    fun downloadCorrectSong() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadActivity::class.java)
        val scn: ActivityScenario<DownloadActivity> = ActivityScenario.launch(intent)
        val songName = "imagine dragons believer"

        onView(withId(R.id.download_songName)).perform(typeText(songName), closeSoftKeyboard())
        Thread.sleep(waitForButton)
        onView(withId(R.id.download_downloadsong)).perform(click())

        while(!DownloadActivity.downloadComplete) {
            Thread.sleep(waitForDownload)
        }
        Thread.sleep(waitForButton)

        onView(withId(R.id.download_songName)).check(matches(withText("")))
        onView(withId(R.id.download_songName)).check(matches(withHint("Try another song!")))

        scn.onActivity { activity ->
            val extract = File(activity.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"extract_of_$songName")
            assert(extract.exists())
            extract.delete()

            val records = File(activity.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
            assert(records.exists())
            val text = FileInputStream(records).bufferedReader().use {out -> out.readLine()}
            assertEquals(songName, text)
            records.delete()
        }
    }



    //TODO: test does not work locally and remotely.
    @Test
    fun downloadIncorrectSong() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadActivity::class.java)
        val scn: ActivityScenario<DownloadActivity> = ActivityScenario.launch(intent)
        val songName = "adsfasdgyasdfa"

        onView(withId(R.id.download_songName)).perform(typeText(songName), closeSoftKeyboard())
        Thread.sleep(waitForButton)
        onView(withId(R.id.download_downloadsong)).perform(click())

        while(!DownloadActivity.downloadComplete) {
            Thread.sleep(waitForDownload)
        }
        Thread.sleep(waitForButton)

        onView(withId(R.id.download_songName)).check(matches(withText("")))
        onView(withId(R.id.download_songName)).check(matches(withHint("Please retry!")))

        scn.onActivity { activity ->
            val extract = File(activity.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"extract_of_$songName")
            assert(!extract.exists())
            if(extract.exists()) {
                extract.delete()
            }

            val records = File(activity.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
            assert(!records.exists())
            if(records.exists()) {
                records.delete()
            }
        }
    }

    @Test
    fun downloadExistingSong() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadActivity::class.java)
        val scn: ActivityScenario<DownloadActivity> = ActivityScenario.launch(intent)
        val songName = "imagine dragons believer"

        onView(withId(R.id.download_songName)).perform(typeText(songName), closeSoftKeyboard())
        Thread.sleep(waitForButton)
        onView(withId(R.id.download_downloadsong)).perform(click())

        while(!DownloadActivity.downloadComplete) {
            Thread.sleep(waitForDownload)
        }
        Thread.sleep(waitForButton)

        //Second download attempt

        onView(withId(R.id.download_songName)).perform(typeText(songName), closeSoftKeyboard())
        Thread.sleep(waitForButton)
        onView(withId(R.id.download_downloadsong)).perform(click())

        while(!DownloadActivity.downloadComplete) {
            Thread.sleep(waitForDownload)
        }
        Thread.sleep(waitForButton)

        onView(withId(R.id.download_songName)).check(matches(withText("")))
        onView(withId(R.id.download_songName)).check(matches(withHint("Try a different song!")))

        scn.onActivity { activity ->
            val extract = File(activity.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"extract_of_$songName")
            assert(extract.exists())
            extract.delete()

            val records = File(activity.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
            assert(records.exists())
            val text = FileInputStream(records).bufferedReader().use {out -> out.readLine()}
            assertEquals(songName, text)
            records.delete()
        }
    }

    @Test
    fun checkIntentOnGoBack() {
        onView(withId(R.id.download_to_welcome)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity::class.java.name))
    }

    @Test
    fun checkIntentOnDelete() {
        onView(withId(R.id.download_to_delete)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DeleteSongsActivity::class.java.name))
    }
}
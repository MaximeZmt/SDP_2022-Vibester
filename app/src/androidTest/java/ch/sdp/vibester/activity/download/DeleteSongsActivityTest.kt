/*
package ch.sdp.vibester.activity.download

import android.content.Intent
import android.os.Environment
import android.widget.LinearLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@HiltAndroidTest
class DeleteSongsActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(DeleteSongsActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkIntentOnGoBack() {
        onView(withId(R.id.delete_returnToMain)).perform(click())
        intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun noSongsToDelete() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DeleteSongsActivity::class.java)
        val scn: ActivityScenario<DeleteSongsActivity> = ActivityScenario.launch(intent)

        lateinit var layout: LinearLayout
        scn.onActivity { activity ->
            layout = activity.findViewById(R.id.delete_songs_linear)
        }
        onView(withId(layout.getChildAt(0).id)).check(matches(withText(R.string.delete_nothing_to_delete)))
    }

    @Test
    fun seeExistingSongs() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DeleteSongsActivity::class.java)
        val scn: ActivityScenario<DeleteSongsActivity> = ActivityScenario.launch(intent)

        lateinit var layout: LinearLayout
        scn.onActivity { activity ->
            layout = activity.findViewById(R.id.delete_songs_linear)
        }


        onView(withId(layout.getChildAt(0).id)).check(matches(withText("SONG 1 - ARTIST 1")))
        onView(withId(layout.getChildAt(1).id)).check(matches(withText("SONG 2 - ARTIST 2")))
        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteFirstInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DeleteSongsActivity::class.java)
        val scn: ActivityScenario<DeleteSongsActivity> = ActivityScenario.launch(intent)

        lateinit var layout: LinearLayout
        scn.onActivity { activity ->
            layout = activity.findViewById(R.id.delete_songs_linear)
        }

        onView(withId(layout.getChildAt(0).id)).perform(click())

        assert(!testing1.exists())
        assert(testing2.exists())
        onView(withId(layout.getChildAt(0).id)).check(matches(withText("SONG 2 - ARTIST 2")))

        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteSecondInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DeleteSongsActivity::class.java)
        val scn: ActivityScenario<DeleteSongsActivity> = ActivityScenario.launch(intent)

        lateinit var layout: LinearLayout
        scn.onActivity { activity ->
            layout = activity.findViewById(R.id.delete_songs_linear)
        }

        onView(withId(layout.getChildAt(1).id)).perform(click())

        assert(testing1.exists())
        assert(!testing2.exists())
        onView(withId(layout.getChildAt(0).id)).check(matches(withText("SONG 1 - ARTIST 1")))

        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteBothInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DeleteSongsActivity::class.java)
        val scn: ActivityScenario<DeleteSongsActivity> = ActivityScenario.launch(intent)

        lateinit var layout: LinearLayout
        scn.onActivity { activity ->
            layout = activity.findViewById(R.id.delete_songs_linear)
        }

        onView(withId(layout.getChildAt(0).id)).perform(click())
        onView(withId(layout.getChildAt(0).id)).perform(click())

        assert(!testing1.exists())
        assert(!testing2.exists())
        onView(withId(layout.getChildAt(0).id)).check(matches(withText(R.string.delete_nothing_to_delete)))

        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }
}*/

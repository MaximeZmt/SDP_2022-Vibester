package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Environment
import android.widget.LinearLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
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
        records.appendText("Testing 1\n")
        records.appendText("Testing 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DeleteSongsActivity::class.java)
        val scn: ActivityScenario<DeleteSongsActivity> = ActivityScenario.launch(intent)

        lateinit var layout: LinearLayout
        scn.onActivity { activity ->
            layout = activity.findViewById(R.id.delete_songs_linear)
        }


        onView(withId(layout.getChildAt(0).id)).check(matches(withText("TESTING 1")))
        onView(withId(layout.getChildAt(1).id)).check(matches(withText("TESTING 2")))
        records.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteFirstInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Testing 1\n")
        records.appendText("Testing 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 2")
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
        onView(withId(layout.getChildAt(0).id)).check(matches(withText("TESTING 2")))

        records.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteSecondInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Testing 1\n")
        records.appendText("Testing 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 2")
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
        onView(withId(layout.getChildAt(0).id)).check(matches(withText("TESTING 1")))

        records.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteBothInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Testing 1\n")
        records.appendText("Testing 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Testing 2")
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
        testing1.delete()
        testing2.delete()
    }
}
package ch.sdp.vibester.activity.download

import android.content.Intent
import android.os.Environment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@HiltAndroidTest
class DownloadManagerActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(DownloadManagerActivity::class.java)

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
    fun seeExistingSongs() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        val properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        val testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        val testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadManagerActivity::class.java)
        val scn: ActivityScenario<DownloadManagerActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.download_song_list)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteOneInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        val properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        val testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        val testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadManagerActivity::class.java)
        val scn: ActivityScenario<DownloadManagerActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.download_song_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickOnViewChild(R.id.song_delete)
            )
        )

        checkRecyclerSubViews(R.id.download_song_list, 0, withText("Song 2 - Artist 2"), R.id.delete_song_name)

        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }

    @Test
    fun deleteAllInTheList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")

        val properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")

        val testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()

        val intent = Intent(ApplicationProvider.getApplicationContext(), DownloadManagerActivity::class.java)
        val scn: ActivityScenario<DownloadManagerActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.download_song_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickOnViewChild(R.id.song_delete)
            )
        )

        onView(withId(R.id.download_empty)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        records.delete()
        properties.delete()
        testing1.delete()
    }

    /**
     * Custom function to handle button clicks inside recycleView
     */
    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click()
            .perform(uiController, view.findViewById(viewId))
    }

    /**
     * Custom functions to match the item views inside Recycle View
     */
    private fun checkRecyclerSubViews(recyclerViewId: Int, position: Int, itemMatcher: Matcher<View?>, subViewId: Int) {
        onView(withId(recyclerViewId)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
            .check(matches(atPositionOnView(position, itemMatcher, subViewId)))
    }

    private fun atPositionOnView(position: Int, itemMatcher: Matcher<View?>, targetViewId: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has view id $itemMatcher at position $position")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder!!.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }
    }
}
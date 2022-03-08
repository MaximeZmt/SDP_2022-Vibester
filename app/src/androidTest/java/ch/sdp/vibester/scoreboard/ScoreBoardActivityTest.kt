package ch.sdp.vibester.scoreboard

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R

@RunWith(AndroidJUnit4::class)
class ScoreBoardActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(ScoreBoardActivity::class.java)

    @Test
    fun recycleViewToViewTest() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun recycleViewClickTest() {
        onView((withId(R.id.recycler_view)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
    }

    @Test
    fun recycleViewScrollDownTest() {
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        val itemCount = recyclerView.adapter?.itemCount
        if(itemCount != null) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount.minus(1)))
        }
    }

    @Test
    fun recycleViewShowItemTest() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))
        val nameItem = "Cinnamon Roll"
        onView(withText(nameItem)).check(matches(isDisplayed()))
    }
}
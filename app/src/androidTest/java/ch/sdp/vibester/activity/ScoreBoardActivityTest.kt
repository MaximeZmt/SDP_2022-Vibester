package ch.sdp.vibester.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreBoardActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(ScoreBoardActivity::class.java)

    @Test
    fun genreShouldDisappearAfterSelected() {
        onView(withId(R.id.genrePerScoreboard)).check(matches(isDisplayed()))
        onView(withId(R.id.rockButton)).perform(click())
        onView(withId(R.id.genrePerScoreboard)).check(matches(not(isDisplayed())))
    }

    @Test
    fun rockBtnShouldSetUpRecycleView() {
        onView(withId(R.id.rockButton)).perform(click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun rockBtnShouldEnableRecycleViewClick() {
        onView(withId(R.id.rockButton)).perform(click())

        onView((withId(R.id.recycler_view)))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2, click()
                )
            )
    }

    @Test
    fun topBtnClick() {
        onView(withId(R.id.topTracksButton)).perform(click())
    }

    @Test
    fun kpopBtnClick() {
        onView(withId(R.id.kpopButton)).perform(click())
    }

    @Test
    fun billieEilishButtonClick() {
        onView(withId(R.id.billieEilishButton)).perform(click())
    }

    @Test
    fun imagineDragonsButtonClick() {
        onView(withId(R.id.imagDragonsButton)).perform(click())
    }

    @Test
    fun btsButtonClick() {
        onView(withId(R.id.btsButton)).perform(click())
    }

    @Test
    fun recycleViewScrollDownTest() {
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        val itemCount = recyclerView.adapter?.itemCount
        if (itemCount != null) {
            onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    itemCount.minus(1)
                )
            )
        }
    }
}
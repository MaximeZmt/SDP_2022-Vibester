package ch.sdp.vibester

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.scoreboard.ScoreBoardActivity

@RunWith(AndroidJUnit4::class)
class ScoreBoardActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(ScoreBoardActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun recycleViewToViewTest() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun recycleViewClickTest() {
        onView((withId(R.id.recycler_view)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
    }

    @After
    fun tearDown() {

    }
}
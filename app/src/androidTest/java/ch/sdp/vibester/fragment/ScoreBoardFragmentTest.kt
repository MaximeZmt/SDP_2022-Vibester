package ch.sdp.vibester.fragment

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScoreBoardFragmentTest {

    @get:Rule(order=0)
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
        launchFragmentInHiltContainer<ScoreBoardFragment>(
            themeResId = R.style.AppTheme
        )
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun genreShouldDisappearAfterSelected() {
        onView(withId(R.id.genrePerScoreboard)).check(matches(isDisplayed()))
        onView(withId(R.id.scoreboard_rockButton)).perform(click())
        onView(withId(R.id.genrePerScoreboard)).check(matches(not(isDisplayed())))
    }

   @Test
    fun rockBtnShouldSetUpRecycleView() {
        onView(withId(R.id.scoreboard_rockButton)).perform(click())
        onView(withId(R.id.scoreboard_content_scrolling)).check(matches(isDisplayed()))
    }



    @Test
    fun clickOnItemShouldGoesToProfileAndDisplaysScores() {
        onView(withId(R.id.scoreboard_rockButton)).perform(click())

        onView((withId(R.id.recycler_view)))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2, click()
                )
            )

        onView(withId(R.id.profileContent)).check(matches(isDisplayed()))
        onView(withId(R.id.profileStatistics)).check(matches(isDisplayed()))
    }



    @Test
    fun topBtnClick() {
        onView(withId(R.id.scoreboard_topTracksButton)).perform(click())
    }


    @Test
    fun kpopBtnClick() {
        onView(withId(R.id.scoreboard_kpopButton)).perform(click())
    }



    @Test
    fun billieEilishButtonClick() {
        onView(withId(R.id.scoreboard_billieEilishButton)).perform(click())
    }

    @Test
    fun imagineDragonsButtonClick() {
        onView(withId(R.id.scoreboard_imagDragonsButton)).perform(click())
    }

    @Test
    fun btsButtonClick() {
        onView(withId(R.id.scoreboard_btsButton)).perform(click())
    }
}

package ch.sdp.vibester

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GenreTemporaryTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        GenreTemporary::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun genreLayoutIsDisplayed() {
        onView(withId(R.id.rockButton)).check(matches(isDisplayed()))
        onView(withId(R.id.kpopButton)).check(matches(isDisplayed()))
        onView(withId(R.id.topTracksButton)).check(matches(isDisplayed()))
        onView(withId(R.id.btsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.imagDragonsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.billieEilishButton)).check(matches(isDisplayed()))
    }

}
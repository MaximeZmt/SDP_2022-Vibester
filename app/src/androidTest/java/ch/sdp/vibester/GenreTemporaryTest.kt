package ch.sdp.vibester

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


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
    fun GenreLayoutIsDisplayed() {
        onView(withId(R.id.rockButton)).check(matches(isDisplayed()))
        onView(withId(R.id.kpopButton)).check(matches(isDisplayed()))
        onView(withId(R.id.topTracksButton)).check(matches(isDisplayed()))
        onView(withId(R.id.btsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.imagDragonsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.billieEilishButton)).check(matches(isDisplayed()))
    }

    @get:Rule
    var exception = ExpectedException.none()

    @Test
    fun rockButtonClick() {
        onView(withId(R.id.rockButton)).perform(click())

    }
    @Test
    fun topButtonClick() {
        onView(withId(R.id.topTracksButton)).perform(click())

    }

    @Test
    fun kpopButtonClick() {
        onView(withId(R.id.kpopButton)).perform(click())
    }
}
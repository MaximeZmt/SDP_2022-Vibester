package ch.sdp.vibester

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun buttonLayoutIsDisplayed() {
        onView(withId(R.id.rock)).check(matches(isDisplayed()))
        onView(withId(R.id.kpop)).check(matches(isDisplayed()))
        onView(withId(R.id.top)).check(matches(isDisplayed()))
    }

    @Test
    fun songsListLayoutIsDisplayed() {
        onView(withId(R.id.listSongs)).check(matches(isDisplayed()))
    }
    @Test
    fun rockButtonClick() {
        onView(withId(R.id.rock)).perform(click())
    }
    @Test
    fun topButtonClick() {
        onView(withId(R.id.top)).perform(click())
    }

    @Test
    fun kpopButtonClick() {
        onView(withId(R.id.kpop)).perform(click())
    }
}
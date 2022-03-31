package ch.sdp.vibester

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.activity.TypingGameActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GenreTemporaryTest {
    private val sleepTime: Long = 2500

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

    @Test
    fun rockButtonClick() {
        onView(withId(R.id.rockButton)).perform(click())
        Thread.sleep(sleepTime)
        intended(hasComponent(TypingGameActivity::class.java.name))
    }

    @Test
    fun topButtonClick() {
        onView(withId(R.id.topTracksButton)).perform(click())
        Thread.sleep(sleepTime)

        intended(hasComponent(TypingGameActivity::class.java.name))
    }

    @Test
    fun kpopButtonClick() {
        onView(withId(R.id.kpopButton)).perform(click())
        Thread.sleep(sleepTime)
        intended(hasComponent(TypingGameActivity::class.java.name))
    }

    @Test
    fun billieEilishButtonClick() {
        onView(withId(R.id.billieEilishButton)).perform(click())
        Thread.sleep(sleepTime)
        intended(hasComponent(TypingGameActivity::class.java.name))
    }

    @Test
    fun imagineDragonsButtonClick() {
        onView(withId(R.id.imagDragonsButton)).perform(click())
        Thread.sleep(sleepTime)
        intended(hasComponent(TypingGameActivity::class.java.name))
    }

    @Test
    fun btsButtonClick() {
        onView(withId(R.id.btsButton)).perform(click())
        Thread.sleep(sleepTime)
        intended(hasComponent(TypingGameActivity::class.java.name))
    }
}
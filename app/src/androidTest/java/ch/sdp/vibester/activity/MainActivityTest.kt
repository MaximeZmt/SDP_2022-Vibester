package ch.sdp.vibester.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.GenreToLyrics
import ch.sdp.vibester.GenreToTyping
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        MainActivity::class.java
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
    fun checkIntentOnWelcome() { //FILLER TESTING
        onView(withId(R.id.placeholder_welcome)).perform(click())
        intended(hasComponent(WelcomeActivity::class.qualifiedName))
    }

    @Test
    fun checkIntentOnGenre() {
        onView(withId(R.id.genreButton)).perform(click())
        intended(hasComponent(GenreToTyping::class.qualifiedName))
    }


    @Test
    fun checkIntentOnLyric() {
        onView(withId(R.id.lyricButton)).perform(click())
        intended(hasComponent(GenreToLyrics::class.qualifiedName))
    }
}
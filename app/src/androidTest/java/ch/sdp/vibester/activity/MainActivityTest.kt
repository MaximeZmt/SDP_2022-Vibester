package ch.sdp.vibester.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun testGameSetupFragmentClick() {
        onView(ViewMatchers.withId(R.id.gameSetupBtn)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.fragment_game_setup)).check(matches(isDisplayed()))
    }

    @Test
    fun testWelcomeScreenFragmentClick() {
        onView(ViewMatchers.withId(R.id.welcomeScreenBtn)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.fragment_welcome_screen)).check(matches(isDisplayed()))
    }

    @Test
    fun testWelcomeScreenFragmentOnStart() {
        onView(ViewMatchers.withId(R.id.fragment_welcome_screen)).check(matches(isDisplayed()))
    }

}
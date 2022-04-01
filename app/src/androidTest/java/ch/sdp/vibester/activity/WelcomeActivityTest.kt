package ch.sdp.vibester.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WelcomeActivityTest {
    @get: Rule
    val activityRule = ActivityScenarioRule(WelcomeActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkIntentOnPlay() {
        onView(withId(R.id.welcome_play)).perform(click())
        intended(hasComponent(GameSetupActivity::class.java.name))
    }

    @Test
    fun checkIntentOnProfile() { 
        onView(withId(R.id.welcome_profile)).perform(click())
        intended(hasComponent(ProfileActivity::class.java.name))
    }

    @Test
    fun checkIntentOnScoreboard() {
        onView(withId(R.id.welcome_scoreboard)).perform(click())
        intended(hasComponent(ScoreBoardActivity::class.java.name))
    }

    @Test
    fun checkIntentOnSettings() { //FILLER TESTING
        onView(withId(R.id.welcome_settings)).perform(click())
        intended(hasComponent(AuthenticationActivity::class.java.name))
    }
}
package ch.sdp.vibester

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import ch.sdp.vibester.scoreboard.ScoreBoardActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {
    @get: Rule
    val activityRule = ActivityScenarioRule(WelcomeScreen::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkIntentOnPlay(){
        onView(withId(R.id.welcome_play)).perform(click())
        intended(hasComponent(GameSetupScreen::class.java.name))
    }

    @Test
    fun checkIntentOnProfile(){ //FILLER TESTING
        onView(withId(R.id.welcome_profile)).perform(click())
        intended(hasComponent(GameSetupScreen::class.java.name))
    }

    @Test
    fun checkIntentOnScoreboard(){
        onView(withId(R.id.welcome_scoreboard)).perform(click())
        intended(hasComponent(ScoreBoardActivity::class.java.name))
    }

    @Test
    fun checkIntentOnListen(){ //FILLER TESTING
        onView(withId(R.id.welcome_listen)).perform(click())
        intended(hasComponent(GameSetupScreen::class.java.name))
    }

    @Test
    fun checkIntentOnSettings(){ //FILLER TESTING
        onView(withId(R.id.welcome_settings)).perform(click())
        intended(hasComponent(GameSetupScreen::class.java.name))
    }

    @Test
    fun checkIntentOnLogin(){ //FILLER TESTING
        onView(withId(R.id.welcome_login)).perform(click())
        intended(hasComponent(GameSetupScreen::class.java.name))
    }
}
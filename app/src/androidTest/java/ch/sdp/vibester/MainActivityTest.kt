package ch.sdp.vibester

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.scoreboard.ScoreBoardActivity
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
    fun mainTest(){
        val inputName = "SDPUser"
        onView(withId(R.id.mainNameInput)).perform(typeText(inputName))
        onView(withId(R.id.mainButton)).perform(click())
        intended(toPackage("ch.sdp.vibester"))
        intended(hasExtra("name", inputName))
    }

    @Test
    fun scoreboardTest() {
        onView(withId(R.id.scoreboardButton)).perform(click())
        intended(hasComponent(ScoreBoardActivity::class.qualifiedName))
    }

    @Test
    fun checkIntentOnWelcome(){ //FILLER TESTING
        onView(withId(R.id.placeholder_welcome)).perform(click())
        intended(hasComponent(WelcomeScreen::class.java.name))
    }

}
package ch.sdp.vibester

import android.app.AlertDialog
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class GamescreenActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        GamescreenActivity::class.java
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
    fun buzzerLayoutIsDisplayed() {
        onView(withId(R.id.buzzersLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun scoresLayoutIsDisplayed() {
        onView(withId(R.id.scoresTable)).check(matches(isDisplayed()))
    }

    // FIXME: Find a way to test the popup showing despite not being a component / make robolectric work
/*
    @Test
    fun clickingButtonLaunchesPopup() {


    }
 */

    /*
     * Currently testing with the *static* values. Change to *dynamic* once the game is correctly
     * implemented and all the data are being sent between activities.
     */
    @Test
    fun checkIntentOnEnding() {
        onView(withId(R.id.go_to_end)).perform(click())
        intended(hasComponent(GameEndingScreen::class.java.name))
        intended(hasExtra("playerName", "Arda"))
        intended(hasExtra("nbIncorrectSong", 3))
        /*intended(hasExtra("incorrect_songs", arrayOf("One", "Two", "Three")))
        intended(hasExtra("names", arrayOf("Hello there",
                                                "Second Stat",
                                                "Third Stat",
                                                "Fourth Stat",
                                                "Fifth Stat")))
        intended(hasExtra("values", arrayOf("General Kenobi",
                                                 "----- *2 -----",
                                                 "----- *3 -----",
                                                 "----- *4 -----",
                                                 "----- *5 -----"))) */

        //Brute-force singular intents. Unable to solve the issue with array intents.
        intended(hasExtra("incorrect_song_1", "One"))
        intended(hasExtra("incorrect_song_2", "Two"))
        intended(hasExtra("incorrect_song_3", "Three"))

        intended(hasExtra("stat_1", "Hello There"))
        intended(hasExtra("stat_res_1", "General Kenobi"))
    }
}
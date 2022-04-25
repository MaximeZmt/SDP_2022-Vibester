package ch.sdp.vibester.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuzzerScreenActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        BuzzerScreenActivity::class.java
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

    @Test
    fun answerIsPresentButInvisibleOnStartup() {
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }
/*
    @Test
    fun clickingBuzzerMakesAnswerVisible() {
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.buzzer_0)).perform(click()) // why does it not find the buzzer???
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.buttonWrong)).perform(click())
    }


    @Test
    fun clickingAnswerButtonsMakesAnswerInvisible() {
        val buttonIdArray = arrayOf(R.id.buttonCorrect, R.id.buttonWrong)
        for (butId in buttonIdArray) {
            onView(withId(R.id.buzzer_0)).perform(click()) // make answer visible first
            onView(withId(butId)).perform(click())
            onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        }
    }
*/

    /*
     * Currently testing with the *static* values. Change to *dynamic* once the game is correctly
     * implemented and all the data are being sent between activities.
     */
    @Test
    fun checkIntentOnEnding() {
        val mockArray = arrayListOf<String>("One", "Two", "Three", "Four", "Five")

        val incArray: ArrayList<String> = mockArray

        val statNames: ArrayList<String> = mockArray

        val statVal: ArrayList<String> = mockArray

        onView(withId(R.id.go_to_end)).perform(click())
        intended(hasComponent(GameEndingActivity::class.java.name))

        intended(hasExtra("playerName", "Arda"))
        intended(hasExtra("nbIncorrectSong", 3))

        intended(hasExtra("str_arr_inc", incArray))
        intended(hasExtra("str_arr_name", statNames))
        intended(hasExtra("str_arr_val", statVal))
    }
}
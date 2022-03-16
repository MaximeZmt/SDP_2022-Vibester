package ch.sdp.vibester

import android.app.AlertDialog
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
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

    @Test
    fun answerIsPresentButInvisibleOnStartup() {
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun clickingBuzzerMakesAnswerVisible() {
        var i = 0
        while (i < 4) {
            onView(withId(i)).perform(click())
            onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            i = i + 1
            onView(withId(R.id.buttonCorrect)).perform(click())
        }
    }

    @Test
    fun clickingAnswerButtonsMakesAnswerInvisible() {
        val buttonIdArray = arrayOf(R.id.buttonCorrect, R.id.buttonWrong)
        for (butId in buttonIdArray) {
            onView(withId(0)).perform(click()) // make answer visible first
            onView(withId(butId)).perform(click())
            onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        }
    }
}
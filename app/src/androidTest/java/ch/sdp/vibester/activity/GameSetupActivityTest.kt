package ch.sdp.vibester.activity


import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GameSetupActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(GameSetupActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }


    @Test
    fun checkDefaultSelectDifficulty() {
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Easy")))
    }

    @Test
    fun checkCustomSelectEasy() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Easy")))
    }

    @Test
    fun checkCustomSelectMedium() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Medium")))
    }

    @Test
    fun checkCustomSelectHard() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Hard")))
    }

    @Test
    fun checkIntentOnProceedEasy() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(BuzzerSetupActivity::class.java.name))
        intended(hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkIntentOnProceedMedium() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(BuzzerSetupActivity::class.java.name))
        intended(hasExtra("Difficulty", "Medium"))
    }

    @Test
    fun checkIntentOnProceedHard() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(BuzzerSetupActivity::class.java.name))
        intended(hasExtra("Difficulty", "Hard"))
    }

}
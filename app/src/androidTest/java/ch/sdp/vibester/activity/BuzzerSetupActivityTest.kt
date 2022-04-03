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
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuzzerSetupActivityTest {
    @get: Rule
    val activityRule = ActivityScenarioRule(BuzzerSetupActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkDefaultSelect() {
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))
    }

    @Test
    fun checkCustomSelectOne() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))
    }

    @Test
    fun checkCustomSelectTwo() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Two")))
    }

    @Test
    fun checkCustomSelectThree() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Three")))
    }

    @Test
    fun checkCustomSelectFour() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Four")))
    }

    @Test
    fun checkIntentOnProceedDefault() {
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 1))
    }

    @Test
    fun checkIntentOnProceedOne() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(scrollTo(),click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 1))
    }

    @Test
    fun checkIntentOnProceedTwo() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(scrollTo(),click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 2))
    }

    @Test
    fun checkIntentOnProceedThree() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(scrollTo(),click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 3))
    }

    @Test
    fun checkIntentOnProceedFour() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(scrollTo(),click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 4))
    }

}
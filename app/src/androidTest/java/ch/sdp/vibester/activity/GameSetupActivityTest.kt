package ch.sdp.vibester.activity


import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
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
    fun checkDefaultSelect() {
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))
    }

    @Test
    fun checkDefaultSelectDifficulty() {
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Easy")))
    }

    @Test
    fun checkCustomSelectOne() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))
    }

    @Test
    fun checkCustomSelectTwo() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Two")))
    }

    @Test
    fun checkCustomSelectThree() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Three")))
    }

    @Test
    fun checkCustomSelectFour() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Four")))
    }

    @Test
    fun checkIntentOnProceedDefault() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Number of players", 1))
    }

    @Test
    fun checkIntentOnProceedOne() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Number of players", 1))
    }

    @Test
    fun checkIntentOnProceedTwo() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Number of players", 2))
    }

    @Test
    fun checkIntentOnProceedThree() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Number of players", 3))
    }

    @Test
    fun checkIntentOnProceedFour() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Number of players", 4))
    }

    @Test
    fun checkCustomSelectEasy() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Easy")))
    }

    @Test
    fun checkCustomSelectMedium() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Medium")))
    }

    @Test
    fun checkCustomSelectHard() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText("Hard")))
    }

    @Test
    fun checkIntentOnProceedEasy() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkIntentOnProceedMedium() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Difficulty", "Medium"))
    }

    @Test
    fun checkIntentOnProceedHard() { //FILLER TEST
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(GamescreenActivity::class.java.name))
        intended(hasExtra("Difficulty", "Hard"))
    }

}
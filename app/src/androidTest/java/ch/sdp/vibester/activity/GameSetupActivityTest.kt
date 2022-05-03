package ch.sdp.vibester.activity


import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class GameSetupActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val activityRule = ActivityScenarioRule(GameSetupActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
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
    fun checkReturnButton() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.gameSetup_returnToMain)).perform(click())
        onView(withId(R.id.gameSetup_returnToMain)).perform(click())
        onView(withId(R.id.gameSetup_returnToMain)).perform(click())
        intended(hasComponent(WelcomeActivity::class.java.name))
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

    @Test
    fun localTypingOnClickHard(){
        onView(withId(R.id.local_typing_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(TypingGameActivity::class.java.name))
        intended(hasExtra("Difficulty", "Hard"))
    }

    @Test
    fun localTypingOnClickMedium(){
        onView(withId(R.id.local_typing_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(TypingGameActivity::class.java.name))
        intended(hasExtra("Difficulty", "Medium"))
    }

    @Test
    fun localTypingOnClickEasy(){
        onView(withId(R.id.local_typing_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(TypingGameActivity::class.java.name))
        intended(hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun localLyricsOnClickHard(){
        onView(withId(R.id.local_lyrics_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())
        intended(hasComponent(LyricsBelongGameActivity::class.java.name))
        intended(hasExtra("Difficulty", "Hard"))
    }

    @Test
    fun rockButtonClick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.rockButton)).perform(click())
    }

    @Test
    fun topButtonClick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.topTracksButton)).perform(click())
    }

    @Test
    fun kpopButtonClick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.kpopButton)).perform(click())
    }

    @Test
    fun billieEilishButtonClick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.billieEilishButton)).perform(click())
    }

    @Test
    fun imagineDragonsButtonClick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.imagDragonsButton)).perform(click())
    }

    @Test
    fun btsButtonClick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
    }
}
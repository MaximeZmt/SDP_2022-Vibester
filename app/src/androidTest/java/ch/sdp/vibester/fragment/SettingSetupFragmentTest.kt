package ch.sdp.vibester.fragment

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.BuzzerSetupActivity
import ch.sdp.vibester.activity.ChoosePartyRoomFragment
import ch.sdp.vibester.activity.LyricsBelongGameActivity
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.api.InternetState
import ch.sdp.vibester.launchFragmentInHiltContainer
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
class SettingSetupFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
        launchFragmentInHiltContainer<SettingSetupFragment>(
            themeResId = R.style.AppTheme
        )
    }

    @Test
    fun checkDefaultSelectDifficulty() {
        onView(withId(R.id.difficulty_spinner))
            .check(matches(withSpinnerText(R.string.GameSetup_easy)))
    }

    @Test
    fun checkDefaultSelectGameSize() {
        onView(withId(R.id.size_spinner))
            .check(matches(withSpinnerText(R.string.one)))
    }
//    @Test
//    fun returnFromGenreToGame() {
//        InternetState.forceOnline()
//        Espresso.onView(ViewMatchers.withId(R.id.game_setup_has_internet))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.local_buzzer_game_button))
//            .perform(ViewActions.scrollTo(), ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.chooseGame))
//            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
//        Espresso.onView(ViewMatchers.withId(R.id.genrePerScoreboard))
//            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
//
//        Espresso.onView(withId(R.id.gameSetup_returnToMain)).perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.genrePerScoreboard))
//            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
//        Espresso.onView(ViewMatchers.withId(R.id.chooseGame))
//            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//    }

    @Test
    fun checkCustomSelectEasy() {
        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(0).perform(ViewActions.click())

        onView(withId(R.id.difficulty_spinner))
            .check(matches(withSpinnerText(R.string.GameSetup_easy)))
    }

    @Test
    fun checkCustomSelectMedium() {
        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click())
        onView(withId(R.id.difficulty_spinner))
            .check(matches(withSpinnerText(R.string.GameSetup_medium)))
    }

    @Test
    fun checkCustomSelectHard() {
        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(2).perform(ViewActions.click())

        onView(withId(R.id.difficulty_spinner))
            .check(matches(withSpinnerText(R.string.GameSetup_hard)))
    }

    @Test
    fun checkCustomSelectOne() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(0).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.one)))
    }

    @Test
    fun checkCustomSelectTwo() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.two)))
    }

    @Test
    fun checkCustomSelectThree() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(2).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.three)))
    }

    @Test
    fun checkCustomSelectFour() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(3).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.four)))
    }

    @Test
    fun checkCustomSelectFive() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(4).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.five)))
    }

    @Test
    fun checkCustomSelectSix() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(5).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.six)))
    }

    @Test
    fun checkCustomSelectSeven() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(6).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.seven)))
    }

    @Test
    fun checkCustomSelectEight() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(7).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.eight)))
    }


    @Test
    fun checkCustomSelectNine() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(8).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.nine)))
    }

    @Test
    fun checkCustomSelectTen() {
        onView(withId(R.id.size_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(9).perform(ViewActions.click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.ten)))
    }


    @Test
    fun checkBTSBuzzerEasyProceed() {

        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(0).perform(ViewActions.click())
        onView(withId(R.id.difficulty_proceed)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(BuzzerSetupActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkBTSTypingEasyProceed() {

        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(0).perform(ViewActions.click())
        onView(withId(R.id.difficulty_proceed)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(TypingGameActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkBTSOnlineBuzzerEasyProceed() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(ViewActions.click())
        onView(withId(R.id.online_buzzer_game_button)).perform(
            ViewActions.scrollTo(),
            ViewActions.click()
        )
        onView(withId(R.id.btsButton)).perform(ViewActions.click())
        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(0).perform(ViewActions.click())
        onView(withId(R.id.difficulty_proceed)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(ChoosePartyRoomFragment::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("Difficulty", "Easy"))
    }


    @Test
    fun checkBTSLyricsEasyProceed() {
        onView(withId(R.id.difficulty_spinner)).perform(ViewActions.click())
        Espresso.onData(Matchers.anything()).atPosition(0).perform(ViewActions.click())
        onView(withId(R.id.difficulty_proceed)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkBTSOnlineEasyProceed() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(ViewActions.click())
        onView(withId(R.id.online_buzzer_game_button)).perform(
            ViewActions.scrollTo(),
            ViewActions.click()
        )
        Intents.intended(IntentMatchers.hasComponent(ChoosePartyRoomFragment::class.java.name))
    }
}
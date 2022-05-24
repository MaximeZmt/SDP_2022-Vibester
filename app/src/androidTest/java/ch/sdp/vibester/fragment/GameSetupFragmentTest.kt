package ch.sdp.vibester.fragment

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.BuzzerSetupActivity
import ch.sdp.vibester.activity.ChoosePartyRoomActivity
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
class GameSetupFragmentTest {

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
        launchFragmentInHiltContainer<GameSetupFragment>(
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

    @Test
    fun internetButtonClick() {
        InternetState.disableForceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.game_setup_has_internet)).check(matches(withText("Internet is on")))
        InternetState.forceOffline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.game_setup_has_internet)).check(matches(withText("Internet is off")))
    }

    @Test
    fun returnFromGenreToGame() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.chooseGame)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.genrePerScoreboard)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        onView(withId(R.id.gameSetup_returnToMain)).perform(click())
        onView(withId(R.id.genrePerScoreboard)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.chooseGame)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun returnFromSettingToGenre() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.genrePerScoreboard)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.chooseSetting)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        onView(withId(R.id.gameSetup_returnToMain)).perform(click())
        onView(withId(R.id.chooseSetting)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.genrePerScoreboard)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun checkCustomSelectEasy() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())

        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText(R.string.GameSetup_easy)))
    }

    @Test
    fun checkCustomSelectMedium() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())

        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText(R.string.GameSetup_medium)))
    }

    @Test
    fun checkCustomSelectHard() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())

        onView(withId(R.id.difficulty_spinner)).check(matches(withSpinnerText(R.string.GameSetup_hard)))
    }

    @Test
    fun checkBTSBuzzerEasyProceed() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())

        intended(hasComponent(BuzzerSetupActivity::class.java.name))
        intended(hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkBTSTypingEasyProceed() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_typing_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())

        intended(hasComponent(TypingGameActivity::class.java.name))
        intended(hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkBTSLyricsEasyProceed() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_lyrics_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())
        onView(withId(R.id.difficulty_proceed)).perform(click())

        intended(hasComponent(LyricsBelongGameActivity::class.java.name))
        intended(hasExtra("Difficulty", "Easy"))
    }

    @Test
    fun checkBTSOnlineEasyProceed(){
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.online_buzzer_game_button)).perform(scrollTo(), click())
        intended(hasComponent(ChoosePartyRoomActivity::class.java.name))
    }

    @Test
    fun checkCustomSelectOne() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.one)))
    }

    @Test
    fun checkCustomSelectTwo() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.two)))
    }

    @Test
    fun checkCustomSelectThree() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.three)))
    }

    @Test
    fun checkCustomSelectFour() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.four)))
    }

    @Test
    fun checkCustomSelectFive() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(4).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.five)))
    }

    @Test
    fun checkCustomSelectSix() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(5).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.six)))
    }

    @Test
    fun checkCustomSelectSeven() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(6).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.seven)))
    }

    @Test
    fun checkCustomSelectEight() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(7).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.eight)))
    }


    @Test
    fun checkCustomSelectNine() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(8).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.nine)))
    }

    @Test
    fun checkCustomSelectTen() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
        onView(withId(R.id.size_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(9).perform(click())

        onView(withId(R.id.size_spinner)).check(matches(withSpinnerText(R.string.ten)))
    }

    @Test
    fun rockButtonClick() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.rockButton)).perform(click())
    }

    @Test
    fun topButtonClick() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
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
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.billieEilishButton)).perform(click())
    }

    @Test
    fun imagineDragonsButtonClick() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.imagDragonsButton)).perform(click())
    }

    @Test
    fun btsButtonClick() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.btsButton)).perform(click())
    }

    @Test
    fun customButtonClick() {
        InternetState.forceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.local_buzzer_game_button)).perform(scrollTo(), click())
        onView(withId(R.id.searchArtist)).perform(typeText("muse"), closeSoftKeyboard())
        onView(withId(R.id.validateSearch)).perform(scrollTo(), click())
    }


}
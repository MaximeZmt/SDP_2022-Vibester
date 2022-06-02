package ch.sdp.vibester.fragment

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
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
    fun internetButtonClick() {
        InternetState.disableForceOnline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.game_setup_has_internet)).check(matches(withText("Internet is on")))

        InternetState.forceOffline()
        onView(withId(R.id.game_setup_has_internet)).perform(click())
        onView(withId(R.id.game_setup_has_internet)).check(matches(withText("Internet is off")))
    }

    @Test
    fun localBuzzerOnlick() {
        onView(withId(R.id.local_buzzer_game_button)).perform(click())
    }

    @Test
    fun localTypingOnClick() {
        onView(withId(R.id.local_typing_game_button)).perform(click())
    }

    @Test
    fun localLyricsOnClick() {
        onView(withId(R.id.local_lyrics_game_button)).perform(click())
    }

    @Test
    fun onlineBuzzerOnClick() {
        onView(withId(R.id.online_buzzer_game_button)).perform(click())
    }



}
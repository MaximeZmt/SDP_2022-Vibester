package ch.sdp.vibester.fragment

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class GenreSetupFragmentTest {
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
        launchFragmentInHiltContainer<GenreSetupFragment>(
            fragmentArgs= bundleOf(Pair("gameManager",GameManager()), Pair("test", true)),
            themeResId = R.style.AppTheme
        )
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_lyrics")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")
    }


    @Test
    fun rockButtonClick() {
        onView(withId(R.id.rockButton)).perform(click())
    }

    @Test
    fun topButtonClick() {
        onView(withId(R.id.topTracksButton)).perform(click())
    }

    @Test
    fun kpopButtonClick() {
        onView(withId(R.id.kpopButton)).perform(click())
    }

    @Test
    fun billieEilishButtonClick() {
        onView(withId(R.id.billieEilishButton)).perform(click())
    }

    @Test
    fun imagineDragonsButtonClick() {
        onView(withId(R.id.imagDragonsButton)).perform(click())
    }

    @Test
    fun btsButtonClick() {
        onView(withId(R.id.btsButton)).perform(click())
    }

    @Test
    fun customButtonClick() {
        onView(withId(R.id.searchArtist))
            .perform(ViewActions.typeText("muse"), closeSoftKeyboard())
        onView(withId(R.id.validateSearch))
            .perform(ViewActions.scrollTo(), click())
    }
}
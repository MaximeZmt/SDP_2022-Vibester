package ch.sdp.vibester.fragment

import android.content.Context
import androidx.core.os.bundleOf
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.PartyRoomActivity
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChoosePartyRoomFragmentTest {

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

        val ctx = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        launchFragmentInHiltContainer<ChoosePartyRoomFragment>(
            fragmentArgs= bundleOf(Pair("gameManager",GameManager()), Pair("test", true)),
            themeResId = R.style.AppTheme
        )
    }


    @Test
    fun correctJoinPartyIntent() {
        onView(ViewMatchers.withId(R.id.roomNameInput)).perform(
            ViewActions.typeText("testRoomName"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.joinParty)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(PartyRoomActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("roomID", "testRoomName"))
        Intents.intended(IntentMatchers.hasExtra("joinRoom", true))
    }

    @Test
    fun correctCreatePartyIntent() {
        onView(ViewMatchers.withId(R.id.roomNameInput)).perform(
            ViewActions.typeText("testRoomName"),
            ViewActions.closeSoftKeyboard()
        )

        onView(ViewMatchers.withId(R.id.createParty)).perform(ViewActions.click())
    }
}
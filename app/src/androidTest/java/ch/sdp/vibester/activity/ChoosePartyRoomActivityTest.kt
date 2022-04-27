package ch.sdp.vibester.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChoosePartyRoomActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(ChoosePartyRoomActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun correctJoinPartyIntent() {
        val roomName = "testRoomName"

        onView(ViewMatchers.withId(R.id.roomNameInput)).perform(
            ViewActions.typeText("testRoomName"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.joinParty)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(PartyRoomActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("roomName", roomName))
        Intents.intended(IntentMatchers.hasExtra("createRoom", false))

    }

    @Test
    fun correctCreatePartyIntent() {
        val roomName = "testRoomName"

        onView(ViewMatchers.withId(R.id.roomNameInput)).perform(
            ViewActions.typeText("testRoomName"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.createParty)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(PartyRoomActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("roomName", roomName))
        Intents.intended(IntentMatchers.hasExtra("createRoom", true))

    }
}
package ch.sdp.vibester.activity

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.PartyRoom
import ch.sdp.vibester.user.User
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
class ChoosePartyRoomActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(ChoosePartyRoomActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @BindValue
    @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockInvocation(partyRoom: PartyRoom) {
        every { mockUsersRepo.createRoom(any()) } answers {
            firstArg<(PartyRoom, String) -> Unit>().invoke(partyRoom, "")
        }


        every { mockUsersRepo.readStartGame(any(), any()) } answers {}

        every { mockUsersRepo.updateRoomField<Boolean>(any(), any(), any()) } answers {}
        every { mockUsersRepo.updateRoomField<MutableList<Pair<String, String>>>(any(), any(), any()) } answers {}

        every { mockUsersRepo.getRoomData(any(), any(), any()) } answers {}

    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun correctJoinPartyIntent() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        val roomName = "testRoomName"
        val mockUserEmailList = mutableListOf("email1, email2")
        val mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom)

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        onView(ViewMatchers.withId(R.id.roomNameInput)).perform(
            ViewActions.typeText("testRoomName"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.joinParty)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(PartyRoomActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("roomID", roomName))
        Intents.intended(IntentMatchers.hasExtra("createRoom", false))
    }

    @Test
    fun correctCreatePartyIntent() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        val roomName = "testRoomName"
        val mockUserEmailList = mutableListOf("email1, email2")
        val mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom)

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        onView(ViewMatchers.withId(R.id.roomNameInput)).perform(
            ViewActions.typeText("testRoomName"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.createParty)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(PartyRoomActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("roomID", roomName))
        Intents.intended(IntentMatchers.hasExtra("createRoom", true))
    }
}
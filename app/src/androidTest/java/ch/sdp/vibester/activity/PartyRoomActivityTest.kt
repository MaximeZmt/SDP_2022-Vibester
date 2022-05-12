package ch.sdp.vibester.activity

import android.content.Intent
import android.provider.Telephony
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
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

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PartyRoomActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @BindValue
    @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockInvocation(partyRoom: PartyRoom) {
        every {mockUsersRepo.createRoom(any(), any())} answers {
            lastArg<(PartyRoom) -> Unit>().invoke(partyRoom)
        }

        every {mockUsersRepo.getRoomData(any(), any())} answers {
            lastArg<(PartyRoom) -> Unit>().invoke(partyRoom)
        }
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun correctCreation() {
        var mockRoomName = "mockName"
        var mockUserEmailList = mutableListOf<String>("email1, email2")
        var mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom)

        val intent = Intent(ApplicationProvider.getApplicationContext(), PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", true)

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.emails))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockUserEmailList.toString())))
    }



    @Test
    fun correctJoin() {
        var mockRoomName = "mockName"
        var mockUserEmailList = mutableListOf<String>("email1, email2")
        var mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom)

        val intent = Intent(ApplicationProvider.getApplicationContext(), PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", false)

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.emails))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockUserEmailList.toString())))
    }

}


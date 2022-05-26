package ch.sdp.vibester.activity


import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.PartyRoom
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

    private fun createMockInvocation(partyRoom: PartyRoom, songList: MutableList<Pair<String, String>>, startGame: Boolean) {
        every {mockUsersRepo.createRoom(any(), any())} answers {
            lastArg<(PartyRoom) -> Unit>().invoke(partyRoom)
        }

        every {mockUsersRepo.getRoomData(any(), any(), any())} answers {
            secondArg<(PartyRoom) -> Unit>().invoke(partyRoom)
            lastArg<(MutableList<Pair<String, String>>) -> Unit>().invoke(songList)
        }

        every { mockUsersRepo.readStartGame(any(), any()) } answers {
            secondArg<(Boolean) -> Unit>().invoke(startGame)
        }

        every { mockUsersRepo.updateSongList(any(), any()) } answers {}

        every { mockUsersRepo.updateStartGame(any(), any())} answers {}
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun correctCreation() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        var mockRoomName = "mockName"
        var mockUserEmailList = mutableListOf<String>("email1, email2")
        var mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        var mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, false)

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")


        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", true)

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.emails))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockUserEmailList.toString())))
    }

    @Test
    fun correctJoin() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        var mockRoomName = "mockName"
        var mockUserEmailList = mutableListOf<String>("email1, email2")
        var mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        var mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, false)

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", false)

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.emails))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockUserEmailList.toString())))
    }

    @Test
    fun correctDifficulty() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        var mockRoomName = "mockRoom"
        var mockUserEmailList = mutableListOf<String>("email1, email2")
        var mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        var mockPartyRoom = PartyRoom()
        val gameManager = GameManager()

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")


        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, true)

        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", true)

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        Intents.intended(IntentMatchers.hasComponent(TypingGameActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("Difficulty", R.string.easy.toString()))
    }

}


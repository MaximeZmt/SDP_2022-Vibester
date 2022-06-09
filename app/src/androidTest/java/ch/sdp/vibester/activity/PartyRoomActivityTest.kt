package ch.sdp.vibester.activity


import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.PartyRoom
import com.google.firebase.auth.FirebaseUser
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

    private fun createMockInvocation(partyRoom: PartyRoom, songList: MutableList<Pair<String, String>>, startGame: Boolean, roomID: String) {
        every { mockUsersRepo.createRoom(any()) } answers {
            firstArg<(PartyRoom, String) -> Unit>().invoke(partyRoom, roomID)
        }

        every { mockUsersRepo.getRoomData(any(), any(), any()) } answers {
            secondArg<(PartyRoom, String) -> Unit>().invoke(partyRoom, roomID)
            lastArg<(MutableList<Pair<String, String>>) -> Unit>().invoke(songList)
        }

        every { mockUsersRepo.readStartGame(any(), any()) } answers {
            secondArg<(Boolean) -> Unit>().invoke(startGame)
        }

        every { mockUsersRepo.updateRoomField<Boolean>(any(), any(), any()) } answers {}
        every { mockUsersRepo.updateRoomField<MutableList<Pair<String, String>>>(any(), any(), any()) } answers {}

        every { mockUsersRepo.getCurrentUser() } returns createMockUser()

    }

    private fun createMockUser(): FirebaseUser {
        val email = "u@u.c"
        val uid = "uid"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid
        return mockUser
    }

    @After
    fun clean() {
        Intents.release()
    }

    private fun setGameManager(numSongs:Int = 1, valid: Boolean = true): GameManager {
        val epilogue = "{\"tracks\":{\"track\":["
        val prologue =
            "], \"@attr\":{\"tag\":\"british\",\"page\":\"1\",\"perPage\":\"1\",\"totalPages\":\"66649\",\"total\":\"66649\"}}}"
        var middle = "{\"name\":\"Monday\",\"artist\":{\"name\":\"Imagine Dragons\"}}"
        if(!valid) middle = "{\"name\":\"TEST_SONG_TEST\",\"artist\":{\"name\":\"TEST_ARTIST_TEST\"}}"
        val gameManager = GameManager()

        var i = 0
        var completeMiddle = middle
        while(i < numSongs-1){
            completeMiddle += ",$middle"
            i++
        }
        gameManager.setGameSongList(epilogue + completeMiddle + prologue, LastfmMethod.BY_TAG.method)

        return gameManager
    }

    @Test
    fun correctCreation() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        val mockRoomName = "mockName"
        val mockUserEmailList = mutableListOf<String>("Email1", "Email2")
        val mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        val mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, false, mockRoomName)

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        val gameManager = setGameManager()
        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", true)
        intent.putExtra("gameManager", gameManager)

        val scn: ActivityScenario<PartyRoomActivity> = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.emails))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockUserEmailList.toString())))
    }

    @Test
    fun correctJoin() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        val mockRoomName = "mockName"
        val mockUserEmailList = mutableListOf<String>("email1, email2")
        val mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        val mockPartyRoom = PartyRoom()

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, false, mockRoomName)

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", false)
        intent.putExtra("gameManager", setGameManager())

        val scn: ActivityScenario<PartyRoomActivity> = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.emails))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockUserEmailList.toString())))
    }

    @Test
    fun correctDifficulty() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        val mockRoomName = "mockRoom"
        val mockUserEmailList = mutableListOf<String>("email1, email2")
        val mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        val mockPartyRoom = PartyRoom()

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")

        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, true, mockRoomName)

        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomName)
        intent.putExtra("createRoom", true)
        intent.putExtra("gameManager", setGameManager())

        val scn: ActivityScenario<PartyRoomActivity> = ActivityScenario.launch(intent)

        Intents.intended(IntentMatchers.hasComponent(TypingGameActivity ::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("Difficulty", 0)) // 0 for easy
    }

    @Test
    fun correctRoomID() {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        val mockRoomID = "mockRoom"
        val mockUserEmailList = mutableListOf<String>("email1, email2")
        val mockSongList = mutableListOf<Pair<String, String>>(Pair("mockSong1", "mockSong2"))
        val mockPartyRoom = PartyRoom()

        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "imagine dragons")


        mockPartyRoom.setEmailList(mockUserEmailList)

        createMockInvocation(mockPartyRoom, mockSongList, false, mockRoomID)

        val intent = Intent(ctx, PartyRoomActivity::class.java)
        intent.putExtra("roomName", mockRoomID)
        intent.putExtra("createRoom", false)
        intent.putExtra("gameManager", setGameManager())
        intent.putExtra("roomID", mockRoomID)

        val scn: ActivityScenario<PartyRoomActivity> = ActivityScenario.launch(intent)

        Espresso.onView(ViewMatchers.withId(R.id.roomId))
            .check(ViewAssertions.matches(ViewMatchers.withText(mockRoomID)))
    }

}


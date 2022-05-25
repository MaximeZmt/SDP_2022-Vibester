package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Song
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TypingGameActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @BindValue
    @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockDataGetter() {
        every { mockUsersRepo.setSubFieldValue(any(), any(), any(), any()) } answers {}
        every { mockUsersRepo.updateFieldInt(any(), any(), any(), any()) } answers {}
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
        every { mockUsersRepo.updateSubFieldInt(any(), any(), any(), any(), any()) } answers {}
    }

    @BindValue @JvmField
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockAuthenticator() {
        val mockUser = createMockUser()
        every { mockAuthenticator.getCurrUser() } returns mockUser
        every { mockAuthenticator.getCurrUID() } returns mockUser.uid
        every { mockAuthenticator.isLoggedIn() } returns true

    }

    private fun createMockUser(): FirebaseUser {
        val email = "u@u.c"
        val uid = "uid"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid
        return mockUser
    }

    private val expectedSize = 200

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
    fun spaceGenTest() {
        val height = 10
        val width = 10
        val spaceeeeee = GameActivity.generateSpace(
            width,
            height,
            ApplicationProvider.getApplicationContext()
        )
        assertEquals(width, spaceeeeee.minimumWidth)
        assertEquals(height, spaceeeeee.minimumHeight)
    }

    @Test
    fun textGenTest() {
        val txtInput = "hello"
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val myText = GameActivity.generateText(txtInput, ctx)

        assertEquals(txtInput, myText.text.toString())
        assertEquals(expectedSize, myText.minHeight)
        assertEquals(ContextCompat.getColor(ctx, R.color.black), myText.textColors.defaultColor)
    }

    @Test
    fun imageGenTest() {
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val mySong = Song.singleSong(inputTxt)
        val intent =
        Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)

        val ctx = ApplicationProvider.getApplicationContext() as Context
        val myTest = GameActivity.generateImage(mySong, ctx)
        assertEquals(expectedSize, myTest.minimumHeight)
        assertEquals(expectedSize, myTest.minimumWidth)
    }

    /*
    @Test
    fun guessLayoutTest() {
        createMockDataGetter()
        createMockAuthenticator()
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """
        val songTest = Song.singleSong(inputTxt)
        val gameManager = setGameManager()

        gameManager.setNextSong()
        gameManager.playSong()
        lateinit var frameLay: FrameLayout

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        // Do not put gameManager as an extra: it causes a RuntimeException
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context

        scn.onActivity { activity ->
            frameLay = activity.guess(songTest, LinearLayout(ctx), ctx, gameManager)
            frameLay.performClick()
        }
        assertEquals(songTest.getArtistName(), gameManager.getCurrentSong().getArtistName())
        assertEquals(songTest.getTrackName(), gameManager.getCurrentSong().getTrackName())
        assertEquals(gameManager.getCorrectSongs().size, 1)
    }
     */

    @Test
    fun checkAnswerCorrectTest() {
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """


        val songTest = Song.singleSong(inputTxt)
        val gameManager = setGameManager()
        gameManager.setNextSong()

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)

        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        createMockDataGetter()
        createMockAuthenticator()
        scn.onActivity { activity ->
            activity.checkAnswer(ctx, songTest, gameManager)
        }
        assertEquals(true, gameManager.getCorrectSongs().size == 1)
    }

    @Test
    fun checkAnswerWrongTest() {
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Test", "collectionName":"Mercury - Act 1", "trackName":"Test", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val songTest = Song.singleSong(inputTxt)
        val gameManager = setGameManager()
        gameManager.setNextSong()

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        createMockDataGetter()
        createMockAuthenticator()
        scn.onActivity { activity ->
            activity.checkAnswer(ctx, songTest, gameManager)
        }
        assertEquals(0, gameManager.getCorrectSongs().size)
    }

    @Test
    fun skipTestTyping() {
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Test", "collectionName":"Mercury - Act 1", "trackName":"Test", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val songTest = Song.singleSong(inputTxt)
        val gameManager = setGameManager()
        gameManager.setNextSong()

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.skip_typing)).check(matches(isDisplayed())).perform(click())
        assertEquals(true, onView(withId(R.id.nextSongTyping)).check(matches(isDisplayed())))
    }

    /**
    * Currently testing with the *static* values. Change to *dynamic* once the game is correctly
    * implemented and all the data are being sent between activities.
    */
    @Test
    fun checkIntentOnEnding() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "BTS")

        createMockDataGetter()
        createMockAuthenticator()
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Test", "collectionName":"Mercury - Act 1", "trackName":"Test", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val songTest = Song.singleSong(inputTxt)
        val gameManager = setGameManager()
        gameManager.setNextSong()
        gameManager.gameSize = 1

        val intent = Intent(ctx, TypingGameActivity::class.java)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)

        scn.onActivity { activity ->
            activity.checkAnswer(ctx, songTest, gameManager)
        }
        val incArray: ArrayList<String> = ArrayList(
            gameManager.getWrongSongs().map { it.getTrackName() + " - " + it.getArtistName() })

        val statNames: ArrayList<String> = arrayListOf("Score")

        val statVal: ArrayList<String> = arrayListOf(gameManager.getCorrectSongs().size.toString())

        Intents.intended(IntentMatchers.hasComponent(GameEndingActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("statNames", statNames))
        Intents.intended(IntentMatchers.hasExtra("statValues", statVal))
        Intents.intended(IntentMatchers.hasExtra("incorrectSongList", incArray))

    }

    @Test
    fun testProgressBarOnZero() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "BTS")
        createMockDataGetter()
        createMockAuthenticator()

        val gameManager = setGameManager()
        assertEquals(gameManager.getSongList().size, 1)

        val intent = Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)


        scn.onActivity { activity ->
            activity.testFirstRound(ctx, gameManager)
            activity.testProgressBar()
        }
        assertEquals(gameManager.numPlayedSongs, 1)
        Thread.sleep(1000)

        val incArray: ArrayList<String> = ArrayList(
            gameManager.getWrongSongs().map { it.getTrackName() + " - " + it.getArtistName() })

        val statNames: ArrayList<String> = arrayListOf("Score")
        val statVal: ArrayList<String> = arrayListOf(gameManager.getCorrectSongs().size.toString())

        Intents.intended(IntentMatchers.hasComponent(GameEndingActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("statNames", statNames))
        Intents.intended(IntentMatchers.hasExtra("statValues", statVal))
        Intents.intended(IntentMatchers.hasExtra("incorrectSongList", incArray))
    }

    @Test
    fun nextButtonOnClick(){
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "BTS")

        createMockDataGetter()
        createMockAuthenticator()
        val gameManager = setGameManager(2)
        assertEquals(gameManager.getSongList().size, 2)

        val intent = Intent(ctx, TypingGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testProgressBar()
        }
        Thread.sleep(1000)

        onView(withId(R.id.nextSongTyping)).check(matches(isDisplayed())).perform(click())
        scn.onActivity { activity ->
            activity.testProgressBar()
        }
        Thread.sleep(1000)

        val statNames: ArrayList<String> = arrayListOf("Score")
        val statVal: ArrayList<String> = arrayListOf(gameManager.getCorrectSongs().size.toString())

        Intents.intended(IntentMatchers.hasComponent(GameEndingActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("statNames", statNames))
        Intents.intended(IntentMatchers.hasExtra("statValues", statVal))
    }

    @Test
    fun firstRoundFail(){
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_typing")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "BTS")

        val gameManager = setGameManager(valid = false)
        val intent = Intent(ctx, TypingGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)

        val statNames: ArrayList<String> = arrayListOf("Score")
        val statVal: ArrayList<String> = arrayListOf(gameManager.getCorrectSongs().size.toString())

        Intents.intended(IntentMatchers.hasComponent(GameEndingActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("statNames", statNames))
        Intents.intended(IntentMatchers.hasExtra("statValues", statVal))
    }

    @Test
    fun testProgressBarColor() {
        val gameManager = setGameManager()
        val intent = Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        var color:ColorStateList = ctx.resources.getColorStateList(R.color.floral_white)

        /* Test progress bar is yellow*/
        scn.onActivity { activity ->
            activity.testProgressBar(15)
        }
        Thread.sleep(1000)
        scn.onActivity { activity->
            color = activity.testProgressBarColor()!!
        }
        assertEquals(color, ctx.resources.getColorStateList(R.color.maximum_yellow_red))

        /* Test progress bar is red*/
        scn.onActivity { activity ->
            activity.testProgressBar(5)
        }
        Thread.sleep(1000)
        scn.onActivity { activity->
            color = activity.testProgressBarColor()!!
        }
        assertEquals(color, ctx.resources.getColorStateList(R.color.light_coral))
    }

}
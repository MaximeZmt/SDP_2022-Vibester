package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.sdp.vibester.R
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LyricsBelongGameActivityTest {
    private val sleepTime: Long = 5000
    private val songName = "Thunder"
    private val artistName = "Imagine Dragons"
    private val speechInputCorrect = "Just a young gun with a quick fuse"
    private val speechInputWrong = "I don't remember the lyrics"
    private val lyrics = "Just a young gun with a quick fuse\n" +
            "I was uptight, wanna let loose\n" +
            "I was dreaming of bigger things in\n" +
            "And wanna leave my own life behind\n" +
            "Not a yes sir, not a follower\n" +
            "Fit the box, fit the mold\n" +
            "Have a seat in the foyer, take a number\n" +
            "I was lightning before the thunder\n" +
            "Thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder\n" +
            "Thunder, feel the thunder\n" +
            "Lightning and the thunder\n" +
            "Thunder, feel the thunder\n" +
            "Lightning and the thunder\n" +
            "Thunder, thunder\n" +
            "Thunder\n" +

            "Kids were laughing in my classes\n" +
            "While I was scheming for the masses\n" +
            "Who do you think you are?\n" +
            "Dreaming 'bout being a big star\n" +
            "You say you're basic, you say you're easy\n" +
            "You're always riding in the back seat\n" +
            "Now I'm smiling from the stage while\n" +
            "You were clapping in the nose bleeds\n" +

            "Thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder\n" +

            "Thunder, feel the thunder\n" +
            "Lightning and the thunder\n" +
            "Thunder, feel the thunder\n" +
            "Lightning and the thunder\n" +
            "Thunder\n" +
            "Thunder, feel the thunder\n" +
            "Lightning and the thunder, thunder\n" +

            "Thunder, feel the thunder\n" +
            "Lightning and the thunder, thunder\n" +
            "Thunder, feel the thunder\n" +
            "Lightning and the thunder, thunder\n" +
            "Thunder, feel the thunder\n" +
            "Lightning and the thunder, thunder\n" +
            "Thunder, feel the thunder (feel the)\n" +
            "Lightning and the thunder, thunder\n" +

            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder\n" +
            "Thunder, thun-, thunder\n" +
            "Thun-thun-thunder, thunder"

    private fun setGameManager() : GameManager {
        val managerTxt = """
            {"tracks":
            {"track":[{"name":"Monday","duration":"259","mbid":"31623cce-9717-4513-9d83-1b5d04e44f9b",
            "url":"https://www.last.fm/music/Oasis/_/Wonderwall",
            "streamable":{"#text":"0","fulltrack":"0"},
            "artist":{"name":"Imagine Dragons","mbid":"ecf9f3a3-35e9-4c58-acaa-e707fba45060","url":"https://www.last.fm/music/Oasis"},
            "image":[{"#text":"https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"small"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"medium"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"large"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png","size":"extralarge"}],
            "@attr":{"rank":"1"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
            """
        val gameManager = GameManager()
        gameManager.setGameSongList(managerTxt, LastfmMethod.BY_TAG.method)

        gameManager.currentSong = getFirstSong() // hard-coded
        return gameManager
    }

    private fun getFirstSong() :Song {
        return Song.singleSong(
            ItunesMusicApi.querySong(songName + " " + artistName, OkHttpClient(), 1).get()
        )
    }

    @get: Rule
    val activityRule = ActivityScenarioRule(LyricsBelongGameActivity::class.java)

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun elementsShouldBeDisplayedOnCreate() {
        onView(withId(R.id.btnSpeak)).check(matches(isDisplayed()))
        onView(withId(R.id.progressBarLyrics)).check(matches(isDisplayed()))
    }

    @Test
    fun handleLyricsNoFoundCorrectly() {
        val gameManager = setGameManager()
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        scn.onActivity { activity ->
            activity.testGetAndCheckLyrics(ctx, "the best song in the world", "Mr.Mystery", "", gameManager)
        }
        //FIXME: API takes a lot of time to process this request
        //comment the following lines if this test fail
        Thread.sleep(sleepTime)
        onView(withId(R.id.nextSongButton)).check(matches(isDisplayed()))
        //song skipped, not consider as wrong
        assertEquals(true, gameManager.getScore() == 0)
        assertEquals(true, gameManager.getWrongSongs().size == 0)
    }

    @Test
    fun shouldUpdateSpeechFromInput() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testUpdateSpeechResult("hey")
        }
        onView(withId(R.id.lyricResult)).check(matches(withText("hey")))
    }

/*    @Test
    fun btnCheckVisibleAfterSpeak() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testUpdateSpeechResult("hey")
        }
        onView(withId(R.id.lyricMatchResult)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }*/
/*
    @Test
    fun checkLyricsCorrectTest() {
        val gameManager = setGameManager()
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testCheckLyrics(speechInputCorrect, lyrics, gameManager)
        }
        Thread.sleep(sleepTime)
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("res: correct")))
        assertEquals(1, gameManager.getCorrectSongs().size)
        assertEquals(1, gameManager.getScore())
    }*/

/*    @Test
    fun checkLyricsWrongTest() {
        val gameManager = setGameManager()
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testCheckLyrics(speechInputWrong, lyrics, gameManager)
        }
        Thread.sleep(sleepTime)
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("res: too bad")))
        assertEquals(true, gameManager.getScore() == 0)
        assertEquals(1, gameManager.getWrongSongs().size)
    }*/

/*    @Test
    fun getAndCheckLyricsGivesCorrectAnswerWhenMatch() {
        val gameManager = setGameManager()
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testGetAndCheckLyrics(songName, artistName, speechInputCorrect, gameManager)
        }
        //FIXME
        // API takes a lot of time to process this request, thus resulting in wrong test output
//        Thread.sleep(10000)
//        onView(withId(R.id.lyricMatchResult)).check(matches(withText("res: correct")))
    }*/

/*
    @Test
    fun checkIntentOnEndingForWrongSong() {
        val gameManager = setGameManager()
        gameManager.setNextSong()
        gameManager.gameSize = 1

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        scn.onActivity { activity ->
            activity.testCheckLyrics(speechInputWrong, lyrics, gameManager)
            activity.playRound(gameManager)
        }
        val incArray: ArrayList<String> = ArrayList(
            gameManager.getWrongSongs().map { it.getTrackName() + " - " + it.getArtistName() })

        val statNames: ArrayList<String> = arrayListOf()
        val statName = "Total Score"
        statNames.addAll(arrayOf(statName, statName, statName, statName, statName))

        val statVal: ArrayList<String> = arrayListOf()
        val score = gameManager.getScore().toString()
        statVal.addAll(arrayOf(score, score, score, score, score))

        Intents.intended(IntentMatchers.hasComponent(GameEndingActivity::class.java.name))

        Intents.intended(IntentMatchers.hasExtra("nbIncorrectSong", 1))

        Intents.intended(IntentMatchers.hasExtra("str_arr_inc", incArray))
        Intents.intended(IntentMatchers.hasExtra("str_arr_name", statNames))
        Intents.intended(IntentMatchers.hasExtra("str_arr_val", statVal))
    }
*/

/*    @Test
    fun checkIntentOnNextRoundForCorrectSong() {
        val gameManager = setGameManager()
        gameManager.gameSize = 1

        var currentArtist = ""
        var currentSong = ""
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        scn.onActivity { activity ->
            activity.playRound(gameManager)
            currentArtist = activity.getArtistName()
            currentSong = activity.getSongName()
        }
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("result will show here")))
        onView(withId(R.id.lyricMatchButton)).check(matches(not(isDisplayed())))
        assertEquals(artistName, currentArtist)
        assertEquals("Monday", currentSong)
        onView(withId(R.id.lyricResult)).check(matches(withText("Say something from Monday - $artistName")))
        onView(withId(R.id.progressBarLyrics)).check(matches(isDisplayed()))
        assertEquals(1, gameManager.nextSongInd)
        assertEquals(1, gameManager.numPlayedSongs)
    }*/

    @Test
    fun setFirstSongTest() {
        val gameManager = setGameManager()
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testSetFirstSong(gameManager)
        }
        assertEquals(getFirstSong().getArtistName(), gameManager.currentSong.getArtistName())
        assertEquals(getFirstSong().getTrackName(), gameManager.currentSong.getTrackName())
    }

/*    @Test
    fun clearResultTest() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testClearResult()
        }
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("result will show here")))
    }*/

}
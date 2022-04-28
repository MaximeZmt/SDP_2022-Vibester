package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.helper.TypingGameManager
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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

    private fun setGameManager(numSongs:Int = 1, valid: Boolean = true): TypingGameManager {
        val epilogue = "{\"tracks\":{\"track\":["
        val prologue =
            "], \"@attr\":{\"tag\":\"british\",\"page\":\"1\",\"perPage\":\"1\",\"totalPages\":\"66649\",\"total\":\"66649\"}}}"
        var middle = "{\"name\":\"Monday\",\"artist\":{\"name\":\"Imagine Dragons\"}}"
        if(!valid) middle = "{\"name\":\"TEST_SONG_TEST\",\"artist\":{\"name\":\"TEST_ARTIST_TEST\"}}"
        val gameManager = TypingGameManager()

        var i = 0
        var completeMiddle = middle
        while(i < numSongs-1){
            completeMiddle += ",$middle"
            i++
        }
        gameManager.setGameSongList(epilogue + completeMiddle + prologue, LastfmMethod.BY_TAG.method)

        return gameManager
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
        /** FIXME: API takes a lot of time to process this request
        comment the following lines if this test fail */
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

    @Test
    fun nextButtonOnClick() {
        val gameManager = setGameManager(2)
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activityRule -> activityRule.testProgressBar() }
        Thread.sleep(1000)

        onView(withId(R.id.nextSongButton)).check(matches(isDisplayed())).perform(click())
        scn.onActivity { activityRule -> activityRule.testProgressBar() }
        Thread.sleep(1000)

        val statNames: ArrayList<String> = arrayListOf()
        val statName = "Total Score"
        statNames.addAll(arrayOf(statName, statName, statName, statName, statName))

        val statVal: ArrayList<String> = arrayListOf()
        val score = "0"
        statVal.addAll(arrayOf(score, score, score, score, score))
        Intents.intended(IntentMatchers.hasComponent(GameEndingActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("nbIncorrectSong", 2))
        Intents.intended(IntentMatchers.hasExtra("str_arr_name", statNames))
        Intents.intended(IntentMatchers.hasExtra("str_arr_val", statVal))
    }

    @Test
    fun btnCheckVisibilityAfterSpeak() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.lyricMatchButton)).check(matches(not(isDisplayed())))
        scn.onActivity { activity ->
            activity.testUpdateSpeechResult("hey")
        }
        onView(withId(R.id.lyricMatchButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun getAndCheckLyricsGivesCorrectAnswerWhenMatch() {
        val gameManager = setGameManager()
        gameManager.setNextSong()
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            LyricsBelongGameActivity::class.java
        )
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        scn.onActivity { activity ->
            activity.testGetAndCheckLyrics(ctx, songName, artistName, speechInputCorrect, gameManager)
        }
        //FIXME: API takes a lot of time to process this request
        //comment the following lines if this test fail
        Thread.sleep(sleepTime)
        assertEquals(true, gameManager.getScore() == 1)
    }

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
            activity.testCheckLyrics(ctx, speechInputWrong, lyrics, gameManager)
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

    @Test
    fun checkIntentOnNextRoundForCorrectSong() {
        val gameManager = setGameManager(2)
        gameManager.setNextSong()

        var currentArtist = ""
        var currentSong = ""
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        scn.onActivity { activity ->
            activity.testStartRound(ctx, gameManager)
            currentArtist = activity.getArtistName()
            currentSong = activity.getSongName()
        }
        onView(withId(R.id.lyricMatchButton)).check(matches(not(isDisplayed())))
        assertEquals(artistName, currentArtist)
        assertEquals("Monday", currentSong)
        onView(withId(R.id.progressBarLyrics)).check(matches(isDisplayed()))
        assertEquals(1, gameManager.nextSongInd)
        assertEquals(1, gameManager.numPlayedSongs)
    }


}
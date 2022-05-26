package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.BuzzerScoreUpdater
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.model.Song
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BuzzerScreenActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(
        BuzzerScreenActivity::class.java
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    private fun setGameManager(numSongs: Int = 1, valid: Boolean = true): GameManager {
        val epilogue = "{\"tracks\":{\"track\":["
        val prologue =
            "], \"@attr\":{\"tag\":\"british\",\"page\":\"1\",\"perPage\":\"1\",\"totalPages\":\"66649\",\"total\":\"66649\"}}}"
        var middle = "{\"name\":\"Monday\",\"artist\":{\"name\":\"Imagine Dragons\"}}"
        if (!valid) middle =
            "{\"name\":\"TEST_SONG_TEST\",\"artist\":{\"name\":\"TEST_ARTIST_TEST\"}}"
        val gameManager = GameManager()

        var i = 0
        var completeMiddle = middle
        while (i < numSongs - 1) {
            completeMiddle += ",$middle"
            i++
        }
        gameManager.setGameSongList(
            epilogue + completeMiddle + prologue,
            LastfmMethod.BY_TAG.method
        )
        return gameManager
    }

    @Test
    fun buzzerLayoutIsDisplayed() {
        onView(withId(R.id.buzzersLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun scoresLayoutIsDisplayed() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")

        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.scoresTable)).check(matches(isDisplayed()))
    }

    @Test
    fun answerAndNextArePresentButInvisibleOnStartup() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")

        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.nextSongBuzzer)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testDisplayOfBuzzersWithNames() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")

        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        // Did the round start?

        onView(withId(R.id.buzzer_0)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.buzzer_1)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.buzzer_2)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.buzzer_3)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun pressingBuzzerDuringRoundMakesAnswerVisible() {
        val ctx = ApplicationProvider.getApplicationContext() as Context

        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")

        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.buzzer_0)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    // FIXME: mediaPlayer not being initialized makes it impossible to run this test
/*
    @Test
    fun timeoutAnswerTest() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_buzzer")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "BTS")

        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)

        val gameManager = setGameManager()
        gameManager.setNextSong()
        intent.putExtra("gameManager", gameManager)

        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.timeoutAnswer(ctx, null, gameManager)
            Assert.assertEquals(false, activity.testGetGameIsOn())
        }
    }
*/
    @Test
    fun skipTest() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)

        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.skip)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        scn.onActivity { activity -> Assert.assertEquals(false, activity.testGetGameIsOn()) }
    }

    @Test
    fun setAnswerButtonCorrectTest() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)

        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.buzzer_0)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        onView(withId(R.id.buttonCorrect)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        scn.onActivity { activity ->
            Assert.assertEquals(1, activity.testGetScoreUpdater().getMap()[R.id.buzzer_0])
            Assert.assertEquals(-1, activity.getPressed())
            Assert.assertEquals(false, activity.testGetGameIsOn())
        }
    }

    @Test
    fun setAnswerButtonWrongTest() {
        val ctx = ApplicationProvider.getApplicationContext() as Context

        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)

        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.buzzer_0)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        onView(withId(R.id.buttonWrong)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        scn.onActivity { activity ->
            Assert.assertEquals(0, activity.testGetScoreUpdater().getMap()[R.id.buzzer_0])
            Assert.assertEquals(-1, activity.getPressed())
            Assert.assertEquals(false, activity.testGetGameIsOn())
        }
    }

    @Test
    fun buttonNextOnClickTest() {
        val mockPlayersNumber = 2
        val mockNameArray = arrayOf("John", "Bob")
        val gameManager = setGameManager(2)
        Assert.assertEquals(gameManager.getSongList().size, 2)

        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testProgressBar()
        }
        Thread.sleep(1000)
        scn.onActivity {   activity ->
            Assert.assertEquals(false, activity.testGetGameIsOn())
        }
        onView(withId(R.id.nextSongBuzzer)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.nextSongBuzzer)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        scn.onActivity {   activity ->
            Assert.assertEquals(true, activity.testGetGameIsOn())
        }
    }

    @Test
    fun prepareWinnerAnnouncementTestNoWinner() {
        val ctx = ApplicationProvider.getApplicationContext() as Context

        val intent =  Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 4
        val mockNameArray = arrayOf("John", "Bob", "Doug", "Mike")

        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)

        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)

        val expectedAnnouncement: String = ctx.getString(R.string.BuzzerScreen_noWinner)
        var testAnnouncement: String = "a"
        scn.onActivity {   activity ->
            testAnnouncement = activity.prepareWinnerAnnouncement(testUpdater)
        }
        Assert.assertEquals(expectedAnnouncement, testAnnouncement)
    }

    @Test
    fun prepareWinnerAnnouncementTestOneWinner() {
        val ctx = ApplicationProvider.getApplicationContext() as Context

        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 4
        val mockNameArray = arrayOf("John", "Bob", "Doug", "Mike")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)

        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 1, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)

        val expectedAnnouncement: String = ctx.getString(R.string.BuzzerScreen_oneWinner) + "Bob"
        var testAnnouncement: String = "a"
        scn.onActivity {   activity ->
            testAnnouncement = activity.prepareWinnerAnnouncement(testUpdater)
        }
        Assert.assertEquals(expectedAnnouncement, testAnnouncement)
    }

    @Test
    fun prepareWinnerAnnouncementTestMoreThanOneWinner() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 4
        val mockNameArray = arrayOf<String>("John", "Bob", "Doug", "Mike")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)

        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)

        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(2, 2, 1, 2)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)

        val expectedAnnouncement: String = ctx.getString(R.string.BuzzerScreen_moreThanOneWinner) + "John and Bob and Mike"
        var testAnnouncement: String = "a"
        scn.onActivity {   activity ->
            testAnnouncement = activity.prepareWinnerAnnouncement(testUpdater)
        }
        Assert.assertEquals(expectedAnnouncement, testAnnouncement)
    }

    @Test
    fun checkIntentOnEnding() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_mode), "local_buzzer")
        AppPreferences.setStr(ctx.getString(R.string.preferences_game_genre), "BTS")

        val intent = Intent(ctx, BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 4
        val mockNameArray = arrayOf("John", "Bob", "Doug", "Mike")
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)

        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)

        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        val expectedMap: HashMap<String, Int> = hashMapOf()
        expectedMap.put("John", 0)
        expectedMap.put("Bob", 0)
        expectedMap.put("Doug", 0)
        expectedMap.put("Mike", 0)

        onView(withId(R.id.go_to_end)).check(matches(isDisplayed())).perform(click())
        intended(hasComponent(GameEndingActivity::class.java.name))
        intended(hasExtra("Winner Name", ctx.getString(R.string.BuzzerScreen_noWinner)))
        intended(hasExtra("Player Scores", expectedMap))
    }
}
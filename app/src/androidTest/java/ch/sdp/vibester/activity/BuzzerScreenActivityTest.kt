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
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.GameManager
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
        val mockNameArray = arrayOfNulls<String>(mockPlayersNumber)
        mockNameArray[0] = "John"
        mockNameArray[1] = "Bob"
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.scoresTable)).check(matches(isDisplayed()))
    }

    @Test
    fun answerAndNextArePresentButInvisibleOnStartup() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOfNulls<String>(mockPlayersNumber)
        mockNameArray[0] = "John"
        mockNameArray[1] = "Bob"
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.nextSongBuzzer)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testDisplayOfBuzzersWithNames() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOfNulls<String>(mockPlayersNumber)
        mockNameArray[0] = "John"
        mockNameArray[1] = "Bob"
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        val gameManager = setGameManager()
        gameManager.setNextSong()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        // Did the round start?
        val ctx = ApplicationProvider.getApplicationContext() as Context
        scn.onActivity { activity ->
            activity.startRoundBuzzer(ctx, gameManager)
        }
        onView(withId(R.id.buzzer_0)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun pressingBuzzerDuringRoundMakesAnswerVisible() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOfNulls<String>(mockPlayersNumber)
        mockNameArray[0] = "John"
        mockNameArray[1] = "Bob"
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        val gameManager = setGameManager()
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)
        // Did the round start?
        val ctx = ApplicationProvider.getApplicationContext() as Context
        onView(withId(R.id.buzzer_0)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
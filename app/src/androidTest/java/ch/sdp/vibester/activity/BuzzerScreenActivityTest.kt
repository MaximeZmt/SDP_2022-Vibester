package ch.sdp.vibester.activity

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
import ch.sdp.vibester.helper.BuzzerGameManager
import ch.sdp.vibester.helper.TypingGameManager
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuzzerScreenActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        BuzzerScreenActivity::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    private fun setGameManager(numSongs:Int = 1, valid: Boolean = true): BuzzerGameManager {
        val epilogue = "{\"tracks\":{\"track\":["
        val prologue =
            "], \"@attr\":{\"tag\":\"british\",\"page\":\"1\",\"perPage\":\"1\",\"totalPages\":\"66649\",\"total\":\"66649\"}}}"
        var middle = "{\"name\":\"Monday\",\"artist\":{\"name\":\"Imagine Dragons\"}}"
        if(!valid) middle = "{\"name\":\"TEST_SONG_TEST\",\"artist\":{\"name\":\"TEST_ARTIST_TEST\"}}"
        val gameManager = BuzzerGameManager()

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
    fun buzzerLayoutIsDisplayed() {
        onView(withId(R.id.buzzersLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun scoresLayoutIsDisplayed() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

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
    fun answerIsPresentButInvisibleOnStartup() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

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
    }
    
    @Test
    fun clickingBuzzerMakesAnswerVisible() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)

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
        onView(withId(R.id.buzzer_0)).perform(click())
        onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.buttonWrong)).perform(click())
    }


    @Test
    fun clickingAnswerButtonsMakesAnswerInvisible() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerScreenActivity::class.java)
        val gameManager = setGameManager()
        // Put mock extras inside
        val mockPlayersNumber = 2
        val mockNameArray = arrayOfNulls<String>(mockPlayersNumber)
        mockNameArray[0] = "John"
        mockNameArray[1] = "Bob"
        intent.putExtra("Number of players", mockPlayersNumber)
        intent.putExtra("Player Names", mockNameArray)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerScreenActivity> = ActivityScenario.launch(intent)

        val buttonIdArray = arrayOf(R.id.buttonCorrect, R.id.buttonWrong)
        for (butId in buttonIdArray) {
            onView(withId(R.id.buzzer_0)).perform(click()) // make answer visible first
            onView(withId(butId)).perform(click())
            onView(withId(R.id.answer)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        }
    }
/*
    /*
     * Currently testing with the *static* values. Change to *dynamic* once the game is correctly
     * implemented and all the data are being sent between activities.
     */
    @Test
    fun checkIntentOnEnding() {
        val mockArray = arrayListOf<String>("One", "Two", "Three", "Four", "Five")

        val incArray: ArrayList<String> = mockArray

        val statNames: ArrayList<String> = mockArray

        val statVal: ArrayList<String> = mockArray

        onView(withId(R.id.go_to_end)).perform(click())
        intended(hasComponent(GameEndingActivity::class.java.name))

        intended(hasExtra("playerName", "Arda"))
        intended(hasExtra("nbIncorrectSong", 3))

        intended(hasExtra("str_arr_inc", incArray))
        intended(hasExtra("str_arr_name", statNames))
        intended(hasExtra("str_arr_val", statVal))
    }

 */
}
package ch.sdp.vibester.activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.game.BuzzerScreenActivity
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.helper.GameManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BuzzerSetupActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(BuzzerSetupActivity::class.java)

    val mockArray = arrayOf("1", "2", "3", "4")
    val editTextIdArray = arrayOf(R.id.namePlayer1, R.id.namePlayer2, R.id.namePlayer3, R.id.namePlayer4)

    /**
     * helper function used in tests
     * @param n: number of names to enter
     */
    private fun enterNames(n: Int) {
        var i = 0
        while (i < n) {
            onView(withId(editTextIdArray[i])).perform(typeText(mockArray[i]), closeSoftKeyboard())
            i += 1
        }
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    /**
     * Helper used in tests
     */
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
    fun variousDefaultStateTest(){
        var res:Int = 0
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)

        //Check default selection
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))

        //Wrong Text to Number
        scn.onActivity { activity ->
            res = activity.textToNumber("Test")
        }
        assertEquals(res, 1)
    }

    @Test
    fun textToNumberCorrect(){
        var res = 0
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            res = activity.textToNumber("One")
        }
        assertEquals(1, res)
        scn.onActivity { activity ->
            res = activity.textToNumber("Two")
        }
        assertEquals(2, res)
        scn.onActivity { activity ->
            res = activity.textToNumber("Three")
        }
        assertEquals(3, res)
        scn.onActivity { activity ->
            res = activity.textToNumber("Four")
        }
        assertEquals(4, res)
    }


    @Test
    fun checkIntentOnProceedOne() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(scrollTo(),click())
        enterNames(1)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 1))
    }

    @Test
    fun checkIntentOnProceedTwo() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(scrollTo(),click())
        enterNames(2)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 2))
    }

    @Test
    fun checkIntentOnProceedThree() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(scrollTo(),click())
        enterNames(3)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 3))
    }

    @Test
    fun checkIntentOnProceedFour() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(scrollTo(),click())
        enterNames(4)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 4))
    }

    @Test
    fun playerNameVisibilityWithInvalidId() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
        val invalidId = -1
        scn.onActivity { activity ->
            activity.updatePlayerNameVisibility(0, invalidId)
        }
        onView(withId(R.id.namePlayer1)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.namePlayer2)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.namePlayer3)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.namePlayer4)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun checkDisplayOfMissingNameAlert() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        intent.putExtra("gameManager", setGameManager())
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)

        // click on next button without entering names => check next button disappears and alert appears
        onView(withId(R.id.nb_players_selected)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        onView(withId(R.id.nb_players_selected)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.missingNameAlert)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        // click on "OK" button in alert => check alert disappears and next button reappears
        onView(withId(R.id.missingNameOk)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        onView(withId(R.id.missingNameAlert)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.nb_players_selected)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
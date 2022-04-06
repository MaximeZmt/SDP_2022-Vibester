package ch.sdp.vibester.activity
import android.content.Context
import android.content.Intent
import android.widget.FrameLayout
import android.widget.LinearLayout
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
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuzzerSetupActivityTest {
    @get: Rule
    val activityRule = ActivityScenarioRule(BuzzerSetupActivity::class.java)

    val mockArray = arrayOf("Player1", "Player2", "Player3", "Player4")
    val editTextIdArray = arrayOf(R.id.namePlayer1, R.id.namePlayer2, R.id.namePlayer3, R.id.namePlayer4)

    /** helper function used in tests
     * @param n: number of names to enter
     */
    private fun enterNames(n: Int) {
        var i = 0
        while (i < n) { onView(withId(editTextIdArray[i])).perform(typeText(mockArray[i]), closeSoftKeyboard())
            i += 1
        }
    }

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkDefaultSelect() {
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))
    }

    @Test
    fun textToNumberWrong(){
        var res:Int = 0
        val intent = Intent(ApplicationProvider.getApplicationContext(), BuzzerSetupActivity::class.java)
        val scn: ActivityScenario<BuzzerSetupActivity> = ActivityScenario.launch(intent)
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
    fun checkCustomSelectOne() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("One")))
    }

    @Test
    fun checkCustomSelectTwo() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Two")))
    }

    @Test
    fun checkCustomSelectThree() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Three")))
    }

    @Test
    fun checkCustomSelectFour() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(scrollTo(),click())
        onView(withId(R.id.nb_player_spinner)).check(matches(withSpinnerText("Four")))
    }

    @Test
    fun checkNoIntentIfMissingName() {
        onView(withId(R.id.missingNameAlert)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        onView(withId(R.id.nb_players_selected)).perform(click())
        onView(withId(R.id.missingNameAlert)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.missingNameOk)).perform(click())
        onView(withId(R.id.missingNameAlert)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))

    }

    @Test
    fun checkIntentOnProceedOne() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(0).perform(scrollTo(),click())
        enterNames(1)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 1))
    }

    @Test
    fun checkIntentOnProceedTwo() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(1).perform(scrollTo(),click())
        enterNames(2)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 2))
    }

    @Test
    fun checkIntentOnProceedThree() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(2).perform(scrollTo(),click())
        enterNames(3)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 3))
    }

    @Test
    fun checkIntentOnProceedFour() {
        onView(withId(R.id.nb_player_spinner)).perform(click())
        onData(Matchers.anything()).atPosition(3).perform(scrollTo(),click())
        enterNames(4)
        onView(withId(R.id.nb_players_selected)).perform(click())
        intended(hasComponent(BuzzerScreenActivity::class.java.name))
        intended(hasExtra("Number of players", 4))
    }
}
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
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameEndingActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(GameEndingActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    private var name = "Arda"
    private var nbInc = 3
    private var incArray: ArrayList<String> = arrayListOf()
    private var statNames: ArrayList<String> = arrayListOf()
    private var statRes: ArrayList<String> = arrayListOf()


    @Test
    fun checkIntentOnCalled() {
        incArray.addAll(arrayOf("One", "Two", "Three"))
        statNames.addAll(arrayOf("Hello there",
            "Second Stat",
            "Third Stat",
            "Fourth Stat",
            "Fifth Stat"))
        statRes.addAll(arrayOf("General Kenobi",
            "----- *2 -----",
            "----- *3 -----",
            "----- *4 -----",
            "----- *5 -----"))

        val intent = Intent(ApplicationProvider.getApplicationContext(), GameEndingActivity::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("nbIncorrectSong", nbInc)

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statRes)

        val scn: ActivityScenario<IncorrectSongsActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.end_stat1)).check(matches(withText(statNames[0])))
        onView(withId(R.id.end_stat1_res)).check(matches(withText(statRes[0])))
        onView(withId(R.id.end_stat2)).check(matches(withText(statNames[1])))
        onView(withId(R.id.end_stat2_res)).check(matches(withText(statRes[1])))
        onView(withId(R.id.end_stat3)).check(matches(withText(statNames[2])))
        onView(withId(R.id.end_stat3_res)).check(matches(withText(statRes[2])))
        onView(withId(R.id.end_stat4)).check(matches(withText(statNames[3])))
        onView(withId(R.id.end_stat4_res)).check(matches(withText(statRes[3])))
        onView(withId(R.id.end_stat5)).check(matches(withText(statNames[4])))
        onView(withId(R.id.end_stat5_res)).check(matches(withText(statRes[4])))
        onView(withId(R.id.end_player_name)).check(matches(withText("Here are the stats for the player $name")))
    }

    @Test
    fun checkIntentOnIncorrectSongs() {
        incArray.addAll(arrayOf("One", "Two", "Three"))
        statNames.addAll(arrayOf("Hello there",
            "Second Stat",
            "Third Stat",
            "Fourth Stat",
            "Fifth Stat"))
        statRes.addAll(arrayOf("General Kenobi",
            "----- *2 -----",
            "----- *3 -----",
            "----- *4 -----",
            "----- *5 -----"))

        val intent = Intent(ApplicationProvider.getApplicationContext(), GameEndingActivity::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("nbIncorrectSong", nbInc)

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statRes)

        val scn: ActivityScenario<GameEndingActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.end_go_to_inc)).perform(click())
        intended(hasExtra("nb_false", nbInc))
        intended(hasExtra("str_arr_inc", incArray))

        intended(hasComponent(IncorrectSongsActivity::class.java.name))
    }
}
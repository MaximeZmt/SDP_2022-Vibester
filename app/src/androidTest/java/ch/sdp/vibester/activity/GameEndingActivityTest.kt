package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ch.sdp.vibester.R
import ch.sdp.vibester.database.AppPreferences
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class GameEndingActivityTest {

    @get:Rule
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


    @Test
    fun checkSoloGameOnCall() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.gameGenre = "BTS"
        AppPreferences.gameMode  = "local_lyrics"

        val incorrectSongList: ArrayList<String> = arrayListOf("Mock 1", "Mock 2")
        val correctSongList: ArrayList<String> = arrayListOf("Mock 3", "Mock 4")

        val statNames: ArrayList<String> = arrayListOf("Mock 1")
        val statValues: ArrayList<String> = arrayListOf("Mock 1")

        val intent = Intent(ctx, GameEndingActivity::class.java)
        intent.putStringArrayListExtra("incorrectSongList", incorrectSongList)
        intent.putStringArrayListExtra("correctSongList", correctSongList)
        intent.putStringArrayListExtra("statValues", statValues)
        intent.putStringArrayListExtra("statNames", statNames)

        val scn: ActivityScenario<GameEndingActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.end_stat1)).check(matches(withText(statNames[0])))
        onView(withId(R.id.end_stat1_res)).check(matches(withText(statValues[0])))
        onView(withId(R.id.end_game_mode)).check(matches(withText("Local lyrics - BTS")))
    }

    @Test
    fun checkMultiplePlayerGameOnCall() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.gameGenre = "BTS"
        AppPreferences.gameMode  = "local_buzzer"

        val intent = Intent(ctx, GameEndingActivity::class.java)
        val scn: ActivityScenario<GameEndingActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.end_game_mode)).check(matches(withText("Local buzzer - BTS")))
    }

    @Test
    fun checkReturnToMain() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        AppPreferences.init(ctx)
        AppPreferences.gameGenre = "BTS"
        AppPreferences.gameMode  = "local_buzzer"

        val intent = Intent(ctx, GameEndingActivity::class.java)
        val scn: ActivityScenario<GameEndingActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.end_returnToMain)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }
}
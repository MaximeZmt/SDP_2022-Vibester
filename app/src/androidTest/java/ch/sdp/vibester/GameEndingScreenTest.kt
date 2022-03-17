package ch.sdp.vibester

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
import ch.sdp.vibester.scoreboard.ScoreBoardActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameEndingScreenTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(GameEndingScreen::class.java)

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
    private var inc: Array<String> = arrayOf("One", "Two", "Three")
    private var stats: Array<String> = arrayOf("Hello there",
        "Second Stat",
        "Third Stat",
        "Fourth Stat",
        "Fifth Stat")
    private var statsRes: Array<String> = arrayOf("General Kenobi",
        "----- *2 -----",
        "----- *3 -----",
        "----- *4 -----",
        "----- *5 -----")


    @Test
    fun checkIntentOnCalled() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), GameEndingScreen::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("nbIncorrectSong", nbInc)
        //intent.putExtra("incorrect_songs", inc)
        //intent.putExtra("Stat_names", stats)
        //intent.putExtra("Stat_values", statsRes)
        intent.putExtra("incorrect_song_1", "One")
        intent.putExtra("incorrect_song_2", "Two")
        intent.putExtra("incorrect_song_3", "Three")

        intent.putExtra("stat_1", "Hello there")
        intent.putExtra("stat_res_1", "General Kenobi")
        val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.end_stat1)).check(matches(withText(stats[0])))
        onView(withId(R.id.end_stat1_res)).check(matches(withText(statsRes[0])))
        onView(withId(R.id.end_player_name)).check(matches(withText("Here are the stats for the player $name")))
    }

    @Test
    fun checkIntentOnIncorrectSongs() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), GameEndingScreen::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("nbIncorrectSong", nbInc)
        //intent.putExtra("incorrect_songs", inc)
        //intent.putExtra("Stat_names", stats)
        //intent.putExtra("Stat_values", statsRes)
        intent.putExtra("incorrect_song_1", "One")
        intent.putExtra("incorrect_song_2", "Two")
        intent.putExtra("incorrect_song_3", "Three")

        intent.putExtra("stat_1", "Hello There")
        intent.putExtra("stat_res_1", "General Kenobi")
        val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.end_go_to_inc)).perform(click())
        intended(hasExtra("nb_false", nbInc))
        //intended(hasExtra("incorrect_songs", inc))
        intended(hasExtra("incorrect_song_1", inc[0]))
        intended(hasExtra("incorrect_song_2", inc[1]))
        intended(hasExtra("incorrect_song_3", inc[2]))
        intended(hasComponent(IncorrectSongsScreen::class.java.name))
    }
}
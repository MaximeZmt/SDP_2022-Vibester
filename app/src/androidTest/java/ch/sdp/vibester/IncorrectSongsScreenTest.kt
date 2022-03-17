package ch.sdp.vibester

import android.content.Intent
import android.content.res.Resources
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IncorrectSongsScreenTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(IncorrectSongsScreen::class.java)

    private var nbInc = 3
    private var inc: Array<String> = arrayOf("One", "Two", "Three")

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkIntentOnCalled() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), IncorrectSongsScreen::class.java)
        intent.putExtra("nbIncorrectSong", nbInc)
        intent.putExtra("incorrect_songs", inc)

        val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)

        if(nbInc != 0) {
            for(x in 0 until nbInc) {
                val resNb: Int = (x+1)
                val resName = "incorrect_song_$resNb"
                //onView(withId(incSongsClass.resources.getIdentifier(resName, "id", incSongsClass.packageName)))
                //    .check(matches(withText(inc[x])))
                //onView(withId(incSongsClass.resources.getIdentifier(resName, "id", incSongsClass.packageName)))
                //    .check(matches(isDisplayed()))
            }
        } else {
            //onView(withId(incSongsClass.resources.getIdentifier("incorrect_song_1", "id", incSongsClass.packageName)))
            //    .check(matches(withText(incSongsClass.resources.getString(R.string.inc_all_correct))))
            //onView(withId(incSongsClass.resources.getIdentifier("incorrect_song_1", "id", incSongsClass.packageName)))
            //    .check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkIntentOnGoBack(){
        onView(withId(R.id.incorrect_songs_back_to_welcome)).perform(click())
        intended(hasComponent(WelcomeScreen::class.java.name))
    }
}
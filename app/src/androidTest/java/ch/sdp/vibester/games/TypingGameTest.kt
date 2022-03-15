package ch.sdp.vibester.games

import android.app.Application
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasImeAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import ch.sdp.vibester.model.Song
import okhttp3.internal.wait
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class TypingGameTest{
    @get: Rule
    val activityRule = ActivityScenarioRule(TypingGame::class.java)


    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    //Weird Test but it's working
    @Test
    fun globalTypingTest(){
        val inputName = "abba sos"
        val view =  Espresso.onView(withId(R.id.yourGuessET))
            .perform(ViewActions.typeText(inputName))
        val currenttime = System.currentTimeMillis()
        while(System.currentTimeMillis() < currenttime + 1000){
            //do nothing
        }
        view.perform(ViewActions.typeText(" "))
        while(System.currentTimeMillis() < currenttime + 3000){
            //do nothing
        }
        Espresso.onView(withId(Int.MAX_VALUE)).perform(click())
        while(System.currentTimeMillis() < currenttime + 3000){
            //do nothing
        }
        Intents.intended(IntentMatchers.toPackage("ch.sdp.vibester"))
        val mysong = Intents.getIntents()[0].extras?.get("song") as Song
        assertEquals("ABBA", mysong.getArtistName())
        assertEquals("SOS", mysong.getTrackName())
    }
}
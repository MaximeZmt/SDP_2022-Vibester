package ch.sdp.vibester.games

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
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
        val inputName = "1574210894"
        Espresso.onView(withId(R.id.yourGuessET))
            .perform(ViewActions.typeText(inputName)).perform(closeSoftKeyboard())
        val currenttime = System.currentTimeMillis()
        while(System.currentTimeMillis() < currenttime + 10000){
            //do nothing
        }
        Espresso.onView(withText("Imagine Dragons - Monday")).perform(scrollTo(), click())
        while(System.currentTimeMillis() < currenttime + 1000){
            //do nothing
        }
        Intents.intended(IntentMatchers.toPackage("ch.sdp.vibester"))
        val mysong = Intents.getIntents()[0].extras?.get("song") as Song
        assertEquals("Imagine Dragons", mysong.getArtistName())
        assertEquals("Monday", mysong.getTrackName())
    }

}
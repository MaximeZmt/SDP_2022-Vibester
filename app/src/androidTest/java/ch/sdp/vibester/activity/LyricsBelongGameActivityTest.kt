package ch.sdp.vibester.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LyricsOVHApiInterface
import ch.sdp.vibester.model.Lyric
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import retrofit2.Call
import java.util.ArrayList

@RunWith(AndroidJUnit4::class)
class LyricsBelongGameActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(LyricsBelongGameActivity::class.java)

    // private val lyricsBelongGameActivityClass = Mockito.spy(LyricsBelongGameActivity::class.java)

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setUp() {
        Intents.init()

            //Mockito.spy(LyricsBelongGameActivity())
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun getLyricsFromAPICorrectly() {
        val service = LyricsOVHApiInterface.create()
        val lyric = service.getLyrics("Imagine Dragons", "Thunder").execute()

        assertThat(
            lyric.body().lyrics?.replace("\n", "")?.replace("\r", ""),
            equalTo(("Just a young gun with a quick fuse\n" +
                    "I was uptight, wanna let loose\n" +
                    "I was dreaming of bigger things in\n" +
                    "And wanna leave my own life behind\n" +
                    "Not a yes sir, not a follower\n" +
                    "Fit the box, fit the mold\n" +
                    "Have a seat in the foyer, take a number\n" +
                    "I was lightning before the thunder\n" +
                    "Thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder, thunder\n" +
                    "Thunder\n" +

                    "Kids were laughing in my classes\n" +
                    "While I was scheming for the masses\n" +
                    "Who do you think you are?\n" +
                    "Dreaming 'bout being a big star\n" +
                    "You say you're basic, you say you're easy\n" +
                    "You're always riding in the back seat\n" +
                    "Now I'm smiling from the stage while\n" +
                    "You were clapping in the nose bleeds\n" +

                    "Thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +

                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +

                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +
                    "Thunder, feel the thunder (feel the)\n" +
                    "Lightning and the thunder, thunder\n" +

                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder").replace("\r", "").replace("\n", ""))
        )
    }

    @Test
    fun btnCheckDoesNotWorkBeforeSpeak() {
        onView(withId(R.id.lyricMatchButton)).perform(click())
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("TextView")))
    }

    @Test
    fun checkLyricsShouldReturnCorrect() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testCheckLyrics("Just a young gun with a quick fuse")
        }
        Thread.sleep(5000)
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("correct")))
    }

    @Test
    fun checkLyricsShouldReturnTooBad() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testCheckLyrics("I don't remember the lyrics")
        }
        Thread.sleep(5000)
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("too bad")))
    }

    //intending(hasComponent(DummyActivity.class.getName())).respondWith(new ActivityResult(resultCode, dataIntent));
    //rule.getActivity().startActivityForResult(new Intent(context,DummyActivity.class));

    //intending(hasAction(equalTo(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)))
    //                .respondWith(new ActivityResult(Activity.RESULT_OK, new Intent().putExtra(
    //                        SpeechRecognizer.RESULTS_RECOGNITION, new String[]{"I am uttering."})));

    /*@Test
    fun speechRecognizerWorks() {
        onView(withId(R.id.btnSpeak)).perform(click())
        val audioIntent = Mockito.mock(Intent::class.java)
        val retList = ArrayList<String>()
        retList.add("Just a young gun with a quick fuse")
        *//*Mockito.`when`(audioIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))
            .thenReturn(retList as ArrayList<String>?)*//*
        intending(hasAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().putExtra(
                SpeechRecognizer.RESULTS_RECOGNITION, retList
            )))
        activityRule.scenario.onActivity {  }
        onView(withId(R.id.lyricResult)).check(matches(withText("Just a young gun with a quick fuse")))
    }*/

    //private val LyricsBelongGameActivityClass = Mockito.spy(LyricsBelongGameActivity())
    /*@Test
    fun checkCorrectnessShowCorrect() {
        *//*val txtFromSpeech = Mockito.mock(TextView::class.java)
        Mockito.`when`(txtFromSpeech.toString()).thenReturn("Just a young gun with a quick fuse")*//*

        *//*val method = lyricsBelongGameActivityClass.javaClass
            .getDeclaredMethod("checkCorrectness", String::class.java, String::class.java)
        method.isAccessible = true
        val parameters = arrayOfNulls<Any>(2)
        parameters[0] = "Just a young gun with a quick fuse\n" +
                "I was uptight, wanna let loose\n" +
                "I was dreaming of bigger things in\n" +
                "And wanna leave my own life behind\n" +
                "Not a yes sir, not a follower\n" +
                "Fit the box, fit the mold\n"
        parameters[1] = "Just a young gun with a quick fuse"
        method.invoke(lyricsBelongGameActivityClass, *parameters)*//*

        //ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val originLyrics = "Just a young gun with a quick fuse\n" +
                "I was uptight, wanna let loose\n" +
                "I was dreaming of bigger things in\n" +
                "And wanna leave my own life behind\n" +
                "Not a yes sir, not a follower\n" +
                "Fit the box, fit the mold\n"
        intent.putExtra("originLyric", originLyrics)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)

    }*/


}
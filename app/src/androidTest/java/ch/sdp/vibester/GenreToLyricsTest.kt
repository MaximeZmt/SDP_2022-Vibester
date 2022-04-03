//package ch.sdp.vibester
//
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.action.ViewActions
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers
//import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import ch.sdp.vibester.activity.LyricsBelongGameActivity
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class GenreToLyricsTest {
//
//    private val sleepTime: Long = 2500
//
//    @get:Rule
//    val testRule = ActivityScenarioRule(
//        GenreToLyrics::class.java
//    )
//
//    @Before
//    fun setUp() {
//        Intents.init()
//    }
//
//    @After
//    fun clean() {
//        Intents.release()
//    }
//
//    @Test
//    fun rockButtonClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.rockButton)).perform(ViewActions.click())
//        Thread.sleep(sleepTime)
//        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
//    }
//
//    @Test
//    fun topButtonClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.topTracksButton)).perform(ViewActions.click())
//        Thread.sleep(sleepTime)
//
//        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
//    }
//
//    @Test
//    fun kpopButtonClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.kpopButton)).perform(ViewActions.click())
//        Thread.sleep(sleepTime)
//        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
//    }
//
//    @Test
//    fun billieEilishButtonClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.billieEilishButton)).perform(ViewActions.click())
//        Thread.sleep(sleepTime)
//        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
//    }
//
//    @Test
//    fun imagineDragonsButtonClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.imagDragonsButton)).perform(ViewActions.click())
//        Thread.sleep(sleepTime)
//        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
//    }
//
//    @Test
//    fun btsButtonClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.btsButton)).perform(ViewActions.click())
//        Thread.sleep(sleepTime)
//        Intents.intended(IntentMatchers.hasComponent(LyricsBelongGameActivity::class.java.name))
//    }
//}
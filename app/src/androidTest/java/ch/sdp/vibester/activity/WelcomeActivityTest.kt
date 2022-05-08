//package ch.sdp.vibester.activity
//
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.Intents.intended
//import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import ch.sdp.vibester.R
//import ch.sdp.vibester.TestMode
//import com.google.firebase.auth.FirebaseAuth
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class WelcomeActivityTest {
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val testRule = ActivityScenarioRule(WelcomeActivity::class.java)
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
//}
package ch.sdp.vibester.profile

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.sdp.vibester.GreetingActivity
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileSetupTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        ProfileSetup::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkProfileLayout() {
        val inputName = "0"
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileSetup::class.java)
        intent.putExtra("userID", inputName)
        val scn: ActivityScenario<ProfileSetup> = ActivityScenario.launch(intent)
        onView(withId(R.id.profileStatistics)).check(matches(isDisplayed()))
        onView(withId(R.id.handle)).check(matches(isDisplayed()))
        onView(withId(R.id.username)).check(matches(isDisplayed()))
        onView(withId(R.id.avatar)).check(matches(isDisplayed()))
    }


    @Test
    fun checkProfileData() {
        val inputName = "0"
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileSetup::class.java)
        intent.putExtra("userID", inputName)
        val scn: ActivityScenario<ProfileSetup> = ActivityScenario.launch(intent)
        onView(withId(R.id.handle)).check(matches(withText("user0")))
        onView(withId(R.id.username)).check(matches(withText("username0")))
        onView(withId(R.id.correctSongs)).check(matches(withText(34)))
        onView(withId(R.id.totalGames)).check(matches(withText(5)))
    }
    
}
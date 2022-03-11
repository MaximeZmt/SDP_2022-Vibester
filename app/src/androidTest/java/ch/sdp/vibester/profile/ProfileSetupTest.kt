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
import java.io.Serializable


@RunWith(AndroidJUnit4::class)
class ProfileSetupTest {

    @Test
    fun checkProfileData() {
        val inputProfile = UserProfile("user0", "username0","bit.ly/3IUnyAF",  5, 8, 34, 2)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileSetup::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileSetup> = ActivityScenario.launch(intent)
        onView(withId(R.id.handle)).check(matches(withText(inputProfile.handle)))
        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))
        onView(withId(R.id.correctSongs)).check(matches(withText(inputProfile.correctSongs.toString())))
        onView(withId(R.id.totalGames)).check(matches(withText(inputProfile.totalGames.toString())))
        onView(withId(R.id.ranking)).check(matches(withText(inputProfile.ranking.toString())))
    }

    @Test
    fun checkProfileLayout() {
        val inputProfile = UserProfile("user0", "username0","bit.ly/3IUnyAF",  5, 8, 34, 2)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileSetup::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileSetup> = ActivityScenario.launch(intent)
        onView(withId(R.id.profileStatistics)).check(matches(isDisplayed()))
        onView(withId(R.id.handle)).check(matches(isDisplayed()))
        onView(withId(R.id.username)).check(matches(isDisplayed()))
//        onView(withId(R.id.avatar)).check(matches(isDisplayed()))
    }
    
}
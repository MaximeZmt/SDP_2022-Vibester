package ch.sdp.vibester.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.sdp.vibester.R
import ch.sdp.vibester.profile.UserProfile
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {

    @Test
    fun checkProfileData() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        Thread.sleep(3_000)
        onView(withId(R.id.handle)).check(matches(withText(inputProfile.handle)))
        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))
        onView(withId(R.id.correctSongs)).check(matches(withText(inputProfile.correctSongs.toString())))
        onView(withId(R.id.totalGames)).check(matches(withText(inputProfile.totalGames.toString())))
        onView(withId(R.id.ranking)).check(matches(withText(inputProfile.ranking.toString())))
    }

    @Test
    fun checkProfileLayout() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        Thread.sleep(3_000)
        onView(withId(R.id.profileStatistics)).check(matches(isDisplayed()))
        onView(withId(R.id.handle)).check(matches(isDisplayed()))
        onView(withId(R.id.username)).check(matches(isDisplayed()))
//        onView(withId(R.id.avatar)).check(matches(isDisplayed()))
    }

    @Test
    fun checkEditProfile() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        val newUsername = "newUser"
        onView(withId(R.id.editUser)).perform(ViewActions.click())
        onView(withId(0)).perform(ViewActions.typeText(newUsername), ViewActions.closeSoftKeyboard())
        onView(withText("OK")).perform(ViewActions.click())
        onView(withId(R.id.username)).check(matches(withText(newUsername)))

    }

    @Test
    fun checkEditProfileClickCancel() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        Thread.sleep(3_000)
        onView(withId(R.id.editUser)).perform(ViewActions.click())
        onView(withText("Cancel")).perform(ViewActions.click())
        onView(withId(R.id.username)).check(matches(withText("Lalisa Bon")))
    }

    @Test
    fun checkEditHandle() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        Thread.sleep(3_000)
        val newUserHandle = "newHandle"
        onView(withId(R.id.editHandle)).perform(ViewActions.click())
        onView(withId(0)).perform(ViewActions.typeText(newUserHandle), ViewActions.closeSoftKeyboard())
        onView(withText("OK")).perform(ViewActions.click())
        onView(withId(R.id.handle)).check(matches(withText("newHandle")))

    }

    @Test
    fun checkEditHandleClickCancel() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("userProfile", inputProfile)
        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        Thread.sleep(3_000)
        onView(withId(R.id.editHandle)).perform(ViewActions.click())
        onView(withText("Cancel")).perform(ViewActions.click())
        onView(withId(R.id.handle)).check(matches(withText("@lisa")))
    }
    
}
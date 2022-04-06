package ch.sdp.vibester.activity

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.* //change this import
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.profile.UserProfile
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileActivityTest {

    private val sleepTime: Long = 4000

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @BindValue @JvmField
    val mockUsersRepo = mockk<UsersRepo>()

    private fun createMockInvocation(mockProfile: UserProfile) {
        every { mockUsersRepo.getUserData(any(), any()) } answers {
            secondArg<(UserProfile) -> Unit>().invoke(mockProfile)
        }
        every { mockUsersRepo.updateName(any(), any(), any()) } answers {}
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkProfileData() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("email", inputProfile.email)

        createMockInvocation(inputProfile)

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.handle)).check(matches(withText(inputProfile.handle)))
        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))
        onView(withId(R.id.correctSongs)).check(matches(withText(inputProfile.correctSongs.toString())))
        onView(withId(R.id.totalGames)).check(matches(withText(inputProfile.totalGames.toString())))
        onView(withId(R.id.ranking)).check(matches(withText(inputProfile.ranking.toString())))
    }

    @Test
    fun checkProfileLayout() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("email", inputProfile.email)

        createMockInvocation(inputProfile)

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.profileStatistics)).check(matches(isDisplayed()))
        onView(withId(R.id.handle)).check(matches(isDisplayed()))
        onView(withId(R.id.username)).check(matches(isDisplayed()))
        onView(withId(R.id.avatar)).check(matches(isDisplayed()))
    }

    @Test
    fun checkEditProfile() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("email", inputProfile.email)

        createMockInvocation(inputProfile)

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        val newUsername = "Lalisa Bon idomesniu"
        onView(withId(R.id.editUser)).perform(ViewActions.click())
        onView(withId(0)).perform(
            ViewActions.typeText(newUsername),
            ViewActions.closeSoftKeyboard()
        )
        onView(withText("OK")).perform(ViewActions.click())
        onView(withId(R.id.username)).check(matches(withText(newUsername)))

    }

    @Test
    fun checkEditProfileClickCancel() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("email", inputProfile.email)

        createMockInvocation(inputProfile)

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.editUser)).perform(ViewActions.click())
        onView(withText("Cancel")).perform(ViewActions.click())
        onView(withId(R.id.username)).check(matches(withText("Lalisa Bon")))
    }

    @Test
    fun checkEditHandle() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("email", inputProfile.email)

        createMockInvocation(inputProfile)

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        val newUserHandle = "@lisa"
        onView(withId(R.id.editHandle)).perform(ViewActions.click())
        onView(withId(0)).perform(
            ViewActions.typeText(newUserHandle),
            ViewActions.closeSoftKeyboard()
        )
        onView(withText("OK")).perform(ViewActions.click())
        onView(withId(R.id.handle)).check(matches(withText("@lisa")))

    }

    @Test
    fun checkEditHandleClickCancel() {
        val inputProfile = UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java)
        intent.putExtra("email", inputProfile.email)

        createMockInvocation(inputProfile)

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.editHandle)).perform(ViewActions.click())
        onView(withText("Cancel")).perform(ViewActions.click())
        onView(withId(R.id.handle)).check(matches(withText("@lisa")))
    }

}
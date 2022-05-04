package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @BindValue @JvmField
    val mockAuthenticatior = mockk<FireBaseAuthenticator>()

    private fun createMockAuthenticatorInvocation() {
        val mockUser = createMockUser()
        every { mockAuthenticatior.getCurrUser() } returns mockUser
    }

    private fun createMockUser(): FirebaseUser {
        val email = "mockuser@gmail.com"
        val uid = "mockuseruid"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid
        return mockUser
    }

    @BindValue @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockInvocation(mockProfile: User) {
        every { mockUsersRepo.getUserData(any(), any()) } answers {
            secondArg<(User) -> Unit>().invoke(mockProfile)
        }

        every { mockUsersRepo.getUserData(any())} answers {}

        every { mockUsersRepo.getCurrentUser() } answers {
            null
        }
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkProfileData() {
        val inputProfile = User("Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))
        onView(withId(R.id.correctSongs)).check(matches(withText(inputProfile.correctSongs.toString())))
        onView(withId(R.id.totalGames)).check(matches(withText(inputProfile.totalGames.toString())))
        onView(withId(R.id.ranking)).check(matches(withText(inputProfile.ranking.toString())))
    }

    @Test
    fun clickBackToMain(){
        val inputProfile = User("Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.profile_returnToMain)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity::class.java.name))
    }

    @Test
    fun shouldShowQrCode() {
        val inputProfile = User("Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)

        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.showQRCode)).perform(click())
        onView(withId(R.id.QrCodePage)).check(matches(isDisplayed()))
        onView(withId(R.id.profileContent)).check(matches(not(isDisplayed())))
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()))
    }

    @Test
    fun clickBackToProfile() {
        val inputProfile = User("Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.showQRCode)).perform(click())
        onView(withId(R.id.qrCode_returnToProfile)).perform(click())
        onView(withId(R.id.QrCodePage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.profileContent)).check(matches(isDisplayed()))
    }


    @Test
    fun checkEditProfile() {
        val inputProfile = User("Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        val newUsername = "Lalisa Bon idomesniu"
        onView(withId(R.id.editUser)).perform(click())
        onView(withId(0)).perform(
            ViewActions.typeText(newUsername),
            ViewActions.closeSoftKeyboard()
        )
        onView(withText("OK")).perform(click())
        onView(withId(R.id.username)).check(matches(withText(newUsername)))
    }

    @Test
    fun checkEditProfileClickCancel() {
        val inputProfile = User( "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.editUser)).perform(click())
        onView(withText("Cancel")).perform(click())
        onView(withId(R.id.username)).check(matches(withText("Lalisa Bon")))
    }

    @Test
    fun checkQrCodeGenerator() {
        val inputProfile = User( "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, 29, 0, "VvPB47tQCLdjz3YebilS6h5EXdJ3")
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockInvocation(inputProfile)
        createMockAuthenticatorInvocation()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.showQRCode)).perform(click())
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()))
    }

}

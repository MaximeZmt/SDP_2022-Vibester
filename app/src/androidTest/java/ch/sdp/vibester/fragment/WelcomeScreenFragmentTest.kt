package ch.sdp.vibester.fragment

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.*
import ch.sdp.vibester.activity.profile.MyProfileActivity
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.google.firebase.auth.FirebaseUser
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
class WelcomeScreenFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockAuthenticatorLoggedOut() {
        val mockUser = createMockUser()
        every { mockAuthenticator.getCurrUser() } returns mockUser
        every { mockAuthenticator.isLoggedIn() } returns false
    }

    private fun createMockAuthenticatorLoggedIn() {
        val mockUser = createMockUser()
        every { mockAuthenticator.getCurrUser() } returns mockUser
        every { mockAuthenticator.getCurrUserMail() } returns mockUser.email.toString()
        every { mockAuthenticator.isLoggedIn() } returns true
    }

    private fun createMockUser(): FirebaseUser {
        val email = "u@u.c"
        val uid = "uid"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid
        return mockUser
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkIntentOnMyAccountLoggedOut() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        createMockAuthenticatorLoggedOut()
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.welcome_profile)).perform(click())
        intended(hasComponent(AuthenticationActivity::class.java.name))
    }

    @Test
    fun checkIntentOnMyAccountLoggedIn() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        createMockAuthenticatorLoggedIn()
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.welcome_profile)).perform(click())
        intended(hasComponent(MyProfileActivity::class.java.name))
    }
    @Test
    fun checkIntentOnDownload() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        createMockAuthenticatorLoggedOut()
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.welcome_download)).perform(click())
        intended(hasComponent(DownloadActivity::class.java.name))
    }
}


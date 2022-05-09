package ch.sdp.vibester.activity

import android.content.ComponentName
import androidx.fragment.app.testing.launchFragmentInContainer
import android.content.Intent
import android.os.Build
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
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.example.android.architecture.blueprints.todoapp.HiltTestActivity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, maxSdk = Build.VERSION_CODES.P)
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

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
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
        intended(hasComponent(ProfileActivity::class.java.name))
    }

    @Test
    fun checkIntentOnScoreboard() {
        val intent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        )
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)


        createMockAuthenticatorLoggedOut()
        launchFragmentInContainer<WelcomeScreenFragment>(
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.welcome_scoreboard)).perform(click())
        intended(hasComponent(ScoreBoardActivity::class.java.name))
    }

    @Test
    fun checkIntentOnDownload() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        createMockAuthenticatorLoggedOut()
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.welcome_download)).perform(click())
        intended(hasComponent(DownloadActivity::class.java.name))
    }

    @Test
    fun checkIntentOnSearch() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        createMockAuthenticatorLoggedOut()
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.welcome_search)).perform(click())
        intended(hasComponent(SearchUserActivity::class.java.name))
    }

    @Test
    fun checkIntentOnSearchWithoutTestMode() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        createMockAuthenticatorLoggedOut()
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.welcome_search)).perform(click())
    }
}


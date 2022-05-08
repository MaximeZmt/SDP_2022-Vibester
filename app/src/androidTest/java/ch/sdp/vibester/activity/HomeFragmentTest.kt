package ch.sdp.vibester.activity

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeFragmentTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        )
    }


    @Test
    fun checkIntentOnMyAccountLoggedOut() {
        FirebaseAuth.getInstance().signOut()
        onView(withId(R.id.welcome_profile)).perform(click())
        intended(hasComponent(AuthenticationActivity::class.java.name))
    }

//    @Test
//    fun checkIntentOnMyAccountLoggedIn() {
//        onView(withId(R.id.welcome_profile)).perform(click())
//        intended(hasComponent(ProfileActivity::class.java.name))
//    }

    @Test
    fun checkIntentOnScoreboard() {
        onView(withId(R.id.welcome_scoreboard)).perform(click())
        intended(hasComponent(ScoreBoardActivity::class.java.name))
    }

    @Test
    fun checkIntentOnDownload() {
        onView(withId(R.id.welcome_download)).perform(click())
        intended(hasComponent(DownloadActivity::class.java.name))
    }

    @Test
    fun checkIntentOnSearch() {
        TestMode.setTest()
        onView(withId(R.id.welcome_search)).perform(click())
        intended(hasComponent(SearchUserActivity::class.java.name))
    }

    @Test
    fun checkIntentOnSearchWithoutTestMode() {
        onView(withId(R.id.welcome_search)).perform(click())
    }
}
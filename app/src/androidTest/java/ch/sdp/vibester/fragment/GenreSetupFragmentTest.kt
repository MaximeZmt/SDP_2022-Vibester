package ch.sdp.vibester.fragment

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.api.InternetState
import ch.sdp.vibester.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class GenreSetupFragmentTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
        launchFragmentInHiltContainer<GenreSetupFragment>(
            themeResId = R.style.AppTheme
        )
    }


    @Test
    fun rockButtonClick() {
        onView(withId(R.id.rockButton)).perform(ViewActions.click())
    }

    @Test
    fun topButtonClick() {
        onView(withId(R.id.topTracksButton)).perform(ViewActions.click())
    }

    @Test
    fun kpopButtonClick() {
        onView(withId(R.id.kpopButton)).perform(ViewActions.click())
    }

    @Test
    fun billieEilishButtonClick() {
        onView(withId(R.id.billieEilishButton)).perform(ViewActions.click())
    }

    @Test
    fun imagineDragonsButtonClick() {
        onView(withId(R.id.imagDragonsButton)).perform(ViewActions.click())
    }

    @Test
    fun btsButtonClick() {
        onView(withId(R.id.btsButton)).perform(ViewActions.click())
    }

    @Test
    fun customButtonClick() {
        onView(withId(R.id.searchArtist))
            .perform(ViewActions.typeText("muse"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.validateSearch))
            .perform(ViewActions.scrollTo(), ViewActions.click())
    }
}
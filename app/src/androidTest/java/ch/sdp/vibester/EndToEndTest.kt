package ch.sdp.vibester

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EndToEndTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.sdp.vibester", appContext.packageName)
    }

    @get:Rule
    val testRule = ActivityScenarioRule(
        MainActivity::class.java
    )


    @Test
    fun endToEndTest() {
        // Context of the app under test.
        val name = "SDPUser"
        onView(withId(R.id.mainNameInput)).perform(ViewActions.typeText(name), closeSoftKeyboard())
        onView(withId(R.id.mainButton)).perform(click())
        onView(withId(R.id.greetName)).check(matches(withText("Hello $name!")))
    }

}
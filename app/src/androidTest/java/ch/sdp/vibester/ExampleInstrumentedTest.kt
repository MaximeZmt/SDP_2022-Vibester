package ch.sdp.vibester

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
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
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.sdp.vibester", appContext.packageName)

    }

    @get:Rule
    var testRule = ActivityScenarioRule(
        MainActivity::class.java
    )


    @Test
    fun endToEndTest() {
        // Context of the app under test.
        val name = "Maxime"
        Espresso.onView(withId(R.id.mainNameInput)).perform(ViewActions.typeText(name), closeSoftKeyboard())
        Espresso.onView(withId(R.id.mainButton)).perform(click())
        Espresso.onView(withId(R.id.greetName)).check(matches(withText("Hello $name!")))
    }
    

    @Test
    fun greetingActivity(){
        // Build the result to return when the activity is launched.
        //Intents.init()
        val resultData = Intent()
        val name = "MaximeZmt"
        resultData.putExtra("name", name)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        // Set up result stubbing when an intent sent to "contacts" is seen.
        intending(toPackage("ch.sdp.vibester")).respondWith(result)
        // User action that results in "contacts" activity being launched.
        // Launching activity expects phoneNumber to be returned and displayed.
        //onView(withId(R.id.pickButton)).perform(click())

        // Assert that the data we set up above is shown.
        onView(withId(R.id.greetName)).check(matches(withText(name)))
        //Intents.release()
    }

}
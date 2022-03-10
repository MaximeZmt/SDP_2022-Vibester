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
import kotlin.random.Random


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SignInActivityTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.sdp.vibester", appContext.packageName)
    }

    @get:Rule
    val testRule = ActivityScenarioRule(
        Register::class.java
    )


    @Test
    fun logInCorrect() {
        val username = "john@test.com"
        val password = "password"
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())
        Thread.sleep(3_000)
        onView(withId(R.id.email)).check(matches(withText("john@test.com")))
    }

    @Test
    fun logInIncorrect() {
        val username = "johnyyy@test.com"
        val password = "password"
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())
        Thread.sleep(3_000)
        onView(withId(R.id.email)).check(matches(withText("Authentication error")))
    }

    @Test
    fun createAccountIncorrect() {
        val username = "john@test.com"
        val password = "password"
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())
        Thread.sleep(3_000)
        onView(withId(R.id.email)).check(matches(withText("Authentication error")))
    }

    @Test
    fun createAccountCorrect() {
        val randomInt = Random.nextInt(0, 1000)
        val username = "newUser".plus(randomInt.toString()).plus("@test.com")
        val password = "password"
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())
        Thread.sleep(3_000)
        onView(withId(R.id.email)).check(matches(withText(username)))
    }

    @Test
    fun gooleLogInTest() {

    }

}
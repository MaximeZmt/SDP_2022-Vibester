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
import ch.sdp.vibester.auth.FireBaseAuthenticator
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
        onView(withId(R.id.createAcc)).perform(click())
        Thread.sleep(3_000)
        onView(withId(R.id.email)).check(matches(withText("Authentication error")))
    }

    @Test
    fun stringValidationEmptyUsername() {
        onView(withId(R.id.createAcc)).perform(click())
        onView(withId(R.id.email)).check(matches(withText("Empty email or password")))
    }

    @Test
    fun stringValidationEmptyUPassword() {
        onView(withId(R.id.createAcc)).perform(click())
        onView(withId(R.id.email)).check(matches(withText("Empty email or password")))
    }

    @Test
    fun stringValidationWrongEmail() {
        val username = "john"
        val password = "password"
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())
        onView(withId(R.id.email)).check(matches(withText("Not an email")))
    }

    @Test
    fun stringValidationShorPassword() {
        val username = "john@test.com"
        val password = "12345"
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())
        onView(withId(R.id.email)).check(matches(withText("Password has to be at least 6 symbols")))
    }

    @Test
    fun derinTest() {
        val a = FireBaseAuthenticator()
        a.googleActivityResult(-1,-1,null)
        onView(withId(R.id.email)).check(matches(withText("TextView")))
    }

    @Test
    fun ardaTest() {
        val a = FireBaseAuthenticator()
        a.googleActivityResult(1000,-1,null)
        onView(withId(R.id.email)).check(matches(withText("TextView")))
    }

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
    fun createAccountCorrect() {
        val randomInt = Random.nextInt(0, 10000)
        val password = "password"
        val username = randomInt.toString().plus("@gg.com")
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())
        Thread.sleep(3_000)
        onView(withId(R.id.email)).check(matches(withText(randomInt.toString().plus("@gg.com"))))
    }


}
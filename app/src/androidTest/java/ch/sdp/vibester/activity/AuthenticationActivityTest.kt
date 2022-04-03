package ch.sdp.vibester.activity

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.base.Joiner.on
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
//import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.util.concurrent.Executor
import kotlin.random.Random


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AuthenticationActivityTest {
    private val sleepTime: Long = 5000

    @BindValue
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockTask(succesful: Boolean): Task<AuthResult> {
        val taskResult = mockk<Task<AuthResult>>()

        every {taskResult.isSuccessful} returns succesful
        every {taskResult.addOnCompleteListener(any<Activity>(), any<OnCompleteListener<AuthResult>>())} answers {
            secondArg<OnCompleteListener<AuthResult>>().onComplete(taskResult)
            taskResult
        }

        return taskResult
    }

    private fun createMockUser(email: String) {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
    }

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.sdp.vibester", appContext.packageName)
    }

    @get:Rule
    val testRule = ActivityScenarioRule(
        AuthenticationActivity::class.java
    )

    @Test
    fun logInIncorrect() {
        val username = "johnyyy@test.com"
        val password = "password"

        val mockTask = createMockTask(false)
        every { mockAuthenticator.signIn(username, password) } returns mockTask

        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.email)).check(matches(withText("Authentication error")))
    }

    @Test
    fun createAccountIncorrect() {
        val username = "john@test.com"
        val password = "password"

        val mockTask = createMockTask(false)
        every { mockAuthenticator.signIn(username, password) } returns mockTask

        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())
        Thread.sleep(sleepTime)
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
        a.googleActivityResult(-1, -1, null)
        onView(withId(R.id.email)).check(matches(withText("TextView")))
    }

    @Test
    fun ardaTest() {
        val a = FireBaseAuthenticator()
        a.googleActivityResult(1000, -1, null)
        onView(withId(R.id.email)).check(matches(withText("TextView")))
    }

    @Test
    fun logInCorrect() {
        val username = "lisa@test.com"
        val password = "password"

        val mockTask = createMockTask(true)
        every { mockAuthenticator.signIn(username, password) } returns mockTask

        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())

        createMockUser(username)

        Thread.sleep(1000)
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("email", username))
    }

    @Test
    fun createAccountCorrect() {
        val username = "user@user.com"
        val password = "password"

        val mockTask = createMockTask(true)
        every { mockAuthenticator.createAccount(username, password) } returns mockTask

        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())

        createMockUser(username)

        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("email", username))
    }

}
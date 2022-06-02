package ch.sdp.vibester.activity

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.activity.profile.MyProfileFragment
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.fragment.AuthenticationFragment
import ch.sdp.vibester.fragment.GameSetupFragment
import ch.sdp.vibester.launchFragmentInHiltContainer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
//import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AuthenticationFragmentTest {

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
    }


    @BindValue @JvmField
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockTask(successful: Boolean): Task<AuthResult> {
        val taskResult = mockk<Task<AuthResult>>()

        every { taskResult.isSuccessful } returns successful
        every { taskResult.addOnCompleteListener(any<Activity>(), any<OnCompleteListener<AuthResult>>()) } answers {
            secondArg<OnCompleteListener<AuthResult>>().onComplete(taskResult)
            taskResult
        }

        return taskResult
    }

    private fun createMockUser(email: String, uid: String = "mockUID"): FirebaseUser {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid

        return mockUser
    }

    @Test
    fun useAppContext() {
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.sdp.vibester", appContext.packageName)
    }


    //FIXME incorrect mock of addOnCompleteListener
/*
    @Test
    fun logInIncorrect() {
        val username = "u@u.c"
        val password = "password"

        val mockTask = createMockTask(false)
        every { mockAuthenticator.signIn(username, password) } returns mockTask
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())
    }
    */

    //FIXME incorrect mock of addOnCompleteListener
/*
    @Test
    fun createAccountIncorrect() {
        val username = "u@u.c"
        val password = "password"

        val mockTask = createMockTask(false)
        every { mockAuthenticator.createAccount(username, password) } returns mockTask
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())
    }
*/
    @Test
    fun stringValidationEmptyFields() {
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.createAcc)).perform(click())
        onView(withId(R.id.authentication_status)).check(matches(withText("Empty email or password")))
    }



    @Test
    fun stringValidationWrongEmail() {
        val username = "j"
        val password = "passwo"
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())

        onView(withId(R.id.authentication_status)).check(matches(withText("Not an email")))
    }

    @Test
    fun stringValidationShorPassword() {
        val username = "u@u.c"
        val password = "1"

        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())

        onView(withId(R.id.authentication_status)).check(matches(withText("Password has to be at least 6 symbols")))
    }

    //FIXME incorrect mock of addOnCompleteListener
/*
    @Test
    fun logInCorrect() {
        val username = "u@u.c"
        val password = "passwo"

        val mockTask = createMockTask(true)
        val mockUser = createMockUser(username)
        every { mockAuthenticator.signIn(username, password) } returns mockTask
        every { mockAuthenticator.getCurrUser()} returns mockUser
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.logIn)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(MyProfileFragment::class.java.name))
    }
*/
    //FIXME incorrect mock of addOnCompleteListener
/*
    @Test
    fun createAccountCorrect() {
        val username = "u@u.c"
        val password = "passwo"

        TestMode.setTest()

        val mockTask = createMockTask(true)
        val mockUser = createMockUser(username)
        every { mockAuthenticator.createAccount(username, password) } returns mockTask
        every { mockAuthenticator.getCurrUser()} returns mockUser
        every { mockAuthenticator.getCurrUserMail()} returns mockUser.email.toString()
        every { mockAuthenticator.getCurrUID()} returns mockUser.uid
        launchFragmentInHiltContainer<AuthenticationFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.username)).perform(ViewActions.typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(ViewActions.typeText(password), closeSoftKeyboard())
        onView(withId(R.id.createAcc)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(MyProfileFragment::class.java.name))
    }
*/
}
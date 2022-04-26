package ch.sdp.vibester.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.* //change this import
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User
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
class CreateProfileActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @BindValue @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockInvocation(email: String) {
        every {mockUsersRepo.createUser(any(), any(), any(), any())} answers {
            lastArg<(String) -> Unit>().invoke(email)
        }

        every { mockUsersRepo.getUserData(any()) } answers {
            secondArg<(User) -> Unit>().invoke(User())
        }
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun createAccCorrect() {
        var username = "mockUsername"
        var mockEmail = "mockEmail@test.com"

        val intent = Intent(ApplicationProvider.getApplicationContext(), CreateProfileActivity::class.java)
        intent.putExtra("email", mockEmail)
        intent.putExtra("isUnitTest", true)

        createMockInvocation(mockEmail)

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.accountUsername)).perform(typeText(username),
            closeSoftKeyboard()
        )

       onView(withId(R.id.createButton)).perform(click())

       Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
       Intents.intended(IntentMatchers.hasExtra("email", mockEmail))
       Intents.intended(IntentMatchers.hasExtra("isUnitTest", true))
    }

}
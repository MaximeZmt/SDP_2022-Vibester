package ch.sdp.vibester.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.* //change this import
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.profile.UserProfile
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

    private val sleepTime: Long = 4000

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @BindValue @JvmField
    val mockUsersRepo = mockk<UsersRepo>()

    private fun createMockInvocation(mockProfile: UserProfile) {
        every { mockUsersRepo.getUserData(any(), any()) } answers {
            secondArg<(UserProfile) -> Unit>().invoke(mockProfile)
        }
        every { mockUsersRepo.updateField(any(), any(), any()) } answers {}
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test


}
package ch.sdp.vibester.activity.profile

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
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
class PublicProfileActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @BindValue
    @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    @BindValue
    @JvmField
    val mockImageGetter = mockk<ImageGetter>()

    private fun createMockDataGetter(mockProfile: User) {
        every { mockUsersRepo.getUserData(any(), any()) } answers {
            secondArg<(User) -> Unit>().invoke(mockProfile)
        }
        every {mockUsersRepo.getCurrentUser()} answers {
            null
        }
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
    }

    private fun createMockImageGetter() {
        every {mockImageGetter.fetchImage(any(), any())} answers {
        }
    }

    @Test
    fun checkProfileData() {
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8, uid = "1234")
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, PublicProfileActivity::class.java)
        intent.putExtra("UserId", 1234)

        createMockDataGetter(inputProfile)
        createMockImageGetter()

        val scn: ActivityScenario<PublicProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))

    }
}
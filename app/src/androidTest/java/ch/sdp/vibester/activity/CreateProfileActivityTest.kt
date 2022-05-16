package ch.sdp.vibester.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
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
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockAuthenticator() {
        val mockUser = createMockUser()
        every { mockAuthenticator.getCurrUser() } returns mockUser
        every { mockAuthenticator.getCurrUID() } returns mockUser.uid
    }

    private fun createMockUser(): FirebaseUser {
        val email = "u@u.c"
        val uid = "uid"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid
        return mockUser
    }

    @BindValue @JvmField
    val mockDataGetter = mockk<DataGetter>()

    private fun createMockDataGetter(email: String) {
        every { mockDataGetter.createUser(any(), any(), any(), any()) } answers {
            lastArg<(String) -> Unit>().invoke(email)
        }
        every { mockDataGetter.getUserData(any(), any()) } answers {secondArg<(User) -> Unit>().invoke(User())}
        every { mockDataGetter.getCurrentUser() } answers { null }
        every { mockDataGetter.setSubFieldValue(any(), any(), any(), any()) } answers {}
        every { mockDataGetter.updateFieldInt(any(), any(), any(), any()) } answers {}
        every { mockDataGetter.setFieldValue(any(), any(), any()) } answers {}
        every { mockDataGetter.updateSubFieldInt(any(), any(), any(), any(), any()) } answers {}
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun createAccCorrect() {
        TestMode.setTest()
        var username = "u"
        var mockEmail = "u@u.c"

        val intent = Intent(ApplicationProvider.getApplicationContext(), CreateProfileActivity::class.java)
        intent.putExtra("email", mockEmail)

        createMockDataGetter(mockEmail)
        createMockAuthenticator()

        val scn: ActivityScenario<CreateProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.accountUsername)).perform(typeText(username),
            closeSoftKeyboard()
        )

       onView(withId(R.id.createButton)).perform(click())

       Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
       Intents.intended(IntentMatchers.hasExtra("email", mockEmail))
    }

}
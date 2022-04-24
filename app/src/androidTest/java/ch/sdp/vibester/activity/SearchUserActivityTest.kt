package ch.sdp.vibester.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.UsersRepo
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchUserActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(SearchUserActivity::class.java)

    @BindValue @JvmField
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    @BindValue @JvmField
    val mockUsersRepo = mockk<UsersRepo>()

    private fun createMockUser(email: String): FirebaseUser {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        return mockUser
    }

    private fun createMockUpdate(mockProfile: FirebaseUser) {
        every { mockUsersRepo.updateFieldSubFieldBoolean(mockProfile.uid, any(), any(), any()) } answers {}
    }


    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun recycleViewToViewTest() {
        onView(ViewMatchers.withId(R.id.searchList))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun recycleViewClickTest() {
        onView((ViewMatchers.withId(R.id.searchList)))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click()
                )
            )
    }

    @Test
    fun recycleViewScrollDownTest() {
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        val itemCount = recyclerView.adapter?.itemCount
        if (itemCount != null) {
            onView(ViewMatchers.withId(R.id.searchList)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    itemCount.minus(1)
                )
            )
        }
    }
    @Test
    fun recycleViewCheckEmpty() {
        val inputTxt= "TESTESTESTEST"
        onView(ViewMatchers.withId(R.id.searchUserET)).perform(ViewActions.typeText(inputTxt),
            ViewActions.closeSoftKeyboard())
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        val itemCount = recyclerView.adapter?.itemCount
        assertEquals(itemCount, null)
    }
    @Test
    fun checkAddBtnClick(){
        val usernameUID = "mockUser@email.com"

        val mockUser = createMockUser(usernameUID)
        every { mockAuthenticator.getCurrUser()} returns mockUser

        Thread.sleep(10000)
        onView(ViewMatchers.withId(R.id.searchList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    clickOnViewChild(R.id.addFriendBtn))
            )

        createMockUpdate(mockUser)
    }

    /**
     * Custom function to handle button clicks inside recycleView
     */
    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }
}
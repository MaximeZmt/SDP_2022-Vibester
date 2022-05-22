package ch.sdp.vibester.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.launchFragmentInHiltContainer
import ch.sdp.vibester.user.User
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchUserFragmentNotLoggedInTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)


    @BindValue @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockInvocation() {
        val mockUser1 = User("mockUser1", uid = "mockUser1uid")
        val mockUser2 = User("mockUser2", uid = "mockUser2uid")
        val mockUser3 = User("mockUser3", uid = "mockUser3uid")
        val mockUser = User("mockUser", uid = "mockUseruid", following = mapOf(Pair(mockUser2.uid,true), Pair(mockUser3.uid,true)))

        val mockUIDs = arrayListOf("mockUser1uid","mockUser2uid","mockUser3uid")
        val mockUsers = arrayListOf(mockUser1, mockUser2, mockUser3)

        every { mockUsersRepo.searchByField(any(), any(), any(), any()) } answers  {
            thirdArg<(ArrayList<User>) -> Unit>().invoke(mockUsers)
            lastArg<(ArrayList<String>) -> Unit>().invoke(mockUIDs)
        }

        every { mockUsersRepo.getUserData(any(), any()) } answers {
            secondArg<(User) -> Unit>().invoke(mockUser)
        }

        every { mockUsersRepo.getCurrentUser() } answers { null }

        every { mockUsersRepo.updateSubFieldInt(any(), any(), any(), any(),any()) } answers {}
        every { mockUsersRepo.setSubFieldValue(any(), any(), any(), any()) } answers {}
        every { mockUsersRepo.setFollowing(any(), any()) } answers {}
        every { mockUsersRepo.setUnfollow(any(), any()) } answers {}
    }

    @BindValue
    @JvmField
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockAuthenticator(){
        val mockUser = null
        every { mockAuthenticator.getCurrUser() } returns mockUser
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
        createMockInvocation()
        createMockAuthenticator()
        launchFragmentInHiltContainer<SearchUserFragment>(
            themeResId = R.style.AppTheme
        )
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkAddBtnClick() {

        Espresso.onView(ViewMatchers.withId(R.id.searchList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickOnViewChild(R.id.addFollowingBtn))
            )

        checkRecyclerSubViews(R.id.searchList, 0,
            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), R.id.addFollowingBtn)
        checkRecyclerSubViews(R.id.searchList, 0,
            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE), R.id.addedFollowingIcon)
    }

    /**
     * Custom function to handle button clicks inside recycleView
     */
    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click()
            .perform(uiController, view.findViewById(viewId))
    }

    /**
     * Custom functions to match the item views inside Recycle View
     */
    private fun checkRecyclerSubViews(recyclerViewId: Int, position: Int, itemMatcher: Matcher<View?>, subViewId: Int) {
        Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
            .check(ViewAssertions.matches(atPositionOnView(position, itemMatcher, subViewId)))
    }

    private fun atPositionOnView(position: Int, itemMatcher: Matcher<View?>, targetViewId: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has view id $itemMatcher at position $position")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder!!.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }
    }
}

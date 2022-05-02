package ch.sdp.vibester.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
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
class SearchUserActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue @JvmField
    val mockUsersRepo = mockk<DataGetter>()

    private fun createMockInvocation() {
        val mockUser1 = User("mockUser1", uid = "mockUser1uid")
        val mockUser2 = User("mockUser2", uid = "mockUser2uid")
        val mockUser3 = User("mockUser3", uid = "mockUser3uid")
        val mockUser = User("mockUser", uid = "mockUseruid", friends = mapOf(Pair(mockUser2.uid,true), Pair(mockUser3.uid,true)))

        val mockUsers = arrayListOf<User>(mockUser1, mockUser2, mockUser3)
        every {mockUsersRepo.searchByField(any(), any(), any())} answers {
            lastArg<(ArrayList<User>) -> Unit>().invoke(mockUsers)
        }

        every {mockUsersRepo.getUserData(any(), any())} answers {
            secondArg<(User) -> Unit>().invoke(mockUser)
        }

        every {mockUsersRepo.updateSubFieldInt(any(), any(), any(), any(),any())} answers {}
    }

    @BindValue @JvmField
    val mockAuthenticator = mockk<FireBaseAuthenticator>()

    private fun createMockAuthenticator(){
        val mockUser = createMockUser()
        every {mockAuthenticator.getCurrUser()} returns mockUser
    }

    private fun createMockUser(email: String = "mockEmail@gmail.com", uid: String = "mockUseruid"): FirebaseUser {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.email } returns email
        every { mockUser.uid } returns uid

        return mockUser
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
        val intent = Intent(ApplicationProvider.getApplicationContext(), SearchUserActivity::class.java)

        createMockInvocation()
        createMockAuthenticator()

        val scn: ActivityScenario<SearchUserActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.searchList))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun recycleViewClickTest() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SearchUserActivity::class.java)

        createMockInvocation()
        createMockAuthenticator()

        val scn: ActivityScenario<SearchUserActivity> = ActivityScenario.launch(intent)

        onView((withId(R.id.searchList)))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click()
                )
            )
    }

    @Test
    fun recycleViewScrollDownTest() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SearchUserActivity::class.java)

        createMockInvocation()
        createMockAuthenticator()

        val scn: ActivityScenario<SearchUserActivity> = ActivityScenario.launch(intent)

        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        val itemCount = recyclerView.adapter?.itemCount
        if (itemCount != null) {
            onView(withId(R.id.searchList)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    itemCount.minus(1)
                )
            )
        }
    }
    
    @Test
    fun checkIconVisible(){
        val intent = Intent(ApplicationProvider.getApplicationContext(), SearchUserActivity::class.java)

        createMockInvocation()
        createMockAuthenticator()

        val scn: ActivityScenario<SearchUserActivity> = ActivityScenario.launch(intent)

        checkRecyclerSubViews(R.id.searchList, 2, withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE), R.id.addFriendBtn);
        checkRecyclerSubViews(R.id.searchList, 2, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), R.id.addedFriendIcon);
    }

    @Test
    fun checkAddBtnClick(){
        val intent = Intent(ApplicationProvider.getApplicationContext(), SearchUserActivity::class.java)

        createMockInvocation()
        createMockAuthenticator()

        val scn: ActivityScenario<SearchUserActivity> = ActivityScenario.launch(intent)

        checkRecyclerSubViews(R.id.searchList, 0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), R.id.addFriendBtn);
        checkRecyclerSubViews(R.id.searchList, 0, withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE), R.id.addedFriendIcon);
        onView(withId(R.id.searchList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickOnViewChild(R.id.addFriendBtn))
            )
        checkRecyclerSubViews(R.id.searchList, 0, withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE), R.id.addFriendBtn);
        checkRecyclerSubViews(R.id.searchList, 0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), R.id.addedFriendIcon);
    }

    /**
     * Custom function to handle button clicks inside recycleView
     */
    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById(viewId))
    }

    /**
     * Custom functions to match the item views inside Recycle View
     */
    fun checkRecyclerSubViews( recyclerViewId: Int, position: Int, itemMatcher: Matcher<View?>, subViewId: Int) {
        onView(withId(recyclerViewId)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
            .check(matches(atPositionOnView(position, itemMatcher, subViewId)))
    }

    fun atPositionOnView(position: Int, itemMatcher: Matcher<View?>,  targetViewId: Int): Matcher<View?> {
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
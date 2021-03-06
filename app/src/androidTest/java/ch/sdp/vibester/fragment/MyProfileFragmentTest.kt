package ch.sdp.vibester.fragment

import android.content.Context
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.download.DownloadManagerActivity
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.launchFragmentInHiltContainer
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MyProfileFragmentTest {

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

    private fun createMockAuthenticator() {
        val mockUser = createMockUser()
        every { mockAuthenticator.getCurrUser() } returns mockUser
        every { mockAuthenticator.isLoggedIn() } returns true
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
    val mockUsersRepo = mockk<DataGetter>()

    @BindValue @JvmField
    val mockImageGetter = mockk<ImageGetter>()

    private fun createMockDataGetter(mockProfile: User) {
        every { mockUsersRepo.getUserData(any(), any()) } answers {
            secondArg<(User) -> Unit>().invoke(mockProfile)
        }
        every { mockUsersRepo.getCurrentUser() } answers { null }
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}

        every { mockUsersRepo.setFollowing(any(), any()) } answers {}
        every { mockUsersRepo.setUnfollow(any(), any()) } answers {}
    }

    private fun createMockImageGetter() {
        every { mockImageGetter.fetchImage(any(), any())} answers {}
    }


    @Test
    fun checkChangePhotoCancel() {
        val inputProfile = User( "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, "VvPB47tQCLdjz3YebilS6h5EXdJ3")

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_image_CardView)).perform(click())
        onView(withText("No")).perform(click())

        onView(withId(R.id.profile_image_CardView)).check(matches(isDisplayed()))
    }



    @Test
    fun checkProfileData() {
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))
        onView(withId(R.id.profile_total_games_stat)).check(matches(withText(inputProfile.totalGames.toString())))
    }



    @Test
    fun showScoresByDefault() {
        val scorePerGenre: Map<String, Int> = mapOf(
            "top tracks" to 1,
            "kpop" to 2,
            "rock" to 3,
            "BTS" to 4,
            "Imagine Dragons" to 5,
            "Billie Eilish" to 6)
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",
            12, scores = scorePerGenre)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_top_tracks)).check(matches(withText(inputProfile.scores.getOrDefault("top tracks", 0).toString())))
        onView(withId(R.id.profile_kpop)).check(matches(withText(inputProfile.scores.getOrDefault("kpop", 0).toString())))
        onView(withId(R.id.profile_rock)).check(matches(withText(inputProfile.scores.getOrDefault("rock", 0).toString())))
        onView(withId(R.id.profile_bts)).check(matches(withText(inputProfile.scores.getOrDefault("BTS", 0).toString())))
        onView(withId(R.id.profile_imagine_dragons)).check(matches(withText(inputProfile.scores.getOrDefault("Imagine Dragons", 0).toString())))
        onView(withId(R.id.profile_billie_eilish)).check(matches(withText(inputProfile.scores.getOrDefault("Billie Eilish", 0).toString())))
    }

    @Test
    fun checkFriendsAndScoresBtn() {
        val friendsMap: Map<String, Boolean> = mapOf(
            "friend" to true
        )
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",
            12, following = friendsMap)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_following)).perform(click())
        onView(withId(R.id.profile_scroll_stat)).check(matches(not(isDisplayed())))

        onView(withId(R.id.profile_scores)).perform(click())
        onView(withId(R.id.profile_scroll_stat)).check(matches(isDisplayed()))
    }

    @Test
    fun friendsRecycleViewGoToProfileTest() {
        val friendsMap: Map<String, Boolean> = mapOf(
            "friend1" to true, "friend2" to true
        )
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",
            12, following = friendsMap)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_following)).perform(click())
        onView(withId(R.id.profile_followingList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        onView(withId(R.id.profileLayout)).check(matches(isDisplayed()))
    }


    @Test
    fun shouldShowQrCode() {
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.showQRCode)).perform(click())

        onView(withId(R.id.QrCodePage)).check(matches(isDisplayed()))
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()))
    }


    @Test
    fun clickBackToProfile() {
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.showQRCode)).perform(click())
        onView(withText("CLOSE")).perform(click())

        onView(withId(R.id.QrCodePage)).check(matches(not(isDisplayed())))
    }


    @Test
    fun checkEditProfile() {
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        val newUsername = "Lalisa Bon idomesniu"

        onView(withId(R.id.editUser)).perform(click())
        onView(withId(0)).perform(
            ViewActions.typeText(newUsername),
            ViewActions.closeSoftKeyboard()
        )
        onView(withText("OK")).perform(click())

        onView(withId(R.id.username)).check(matches(withText(newUsername)))
    }


    @Test
    fun checkEditProfileClickCancel() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)
        val ctx = ApplicationProvider.getApplicationContext() as Context

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.editUser)).perform(click())
        onView(withText("Cancel")).perform(click())

        onView(withId(R.id.username)).check(matches(withText("Lalisa Bon")))
    }

    @Test
    fun checkMyMusicGoToDownloadManager() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)
        val ctx = ApplicationProvider.getApplicationContext() as Context

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_localSongs)).perform(click())

        intended(hasComponent(DownloadManagerActivity::class.java.name))
    }




// FIXME fix the test
/*
    @Test
    fun checkQrCodeGenerator() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8,"VvPB47tQCLdjz3YebilS6h5EXdJ3")

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.showQRCode)).perform(click())
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()))

        onView(withId(R.id.qrCode_returnToProfile)).perform(click())
        onView(withId(R.id.profile_image_CardView)).check(matches(isDisplayed()))

        onView(withId(R.id.logout)).perform(scrollTo(), click())
        onView(withId(R.id.main_bottom_nav_fragment)).check(matches(isDisplayed()))
    }
*/

    @Test
    fun checkChangeImage() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8,"VvPB47tQCLdjz3YebilS6h5EXdJ3")

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_image_ImageView)).perform(click())
        onView(withText("NO")).perform(click())
        onView(withId(R.id.profile_image_ImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfPictureIsDisplayed() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8,"VvPB47tQCLdjz3YebilS6h5EXdJ3")

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_image_CardView)).check(matches(isDisplayed()))
    }

    @Test
    fun checkUnfollowBtnClick() {
        val friendsMap: Map<String, Boolean> = mapOf(
            "friend1" to true, "friend2" to true
        )
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",
            12, following = friendsMap)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_following)).perform(click())

        checkRecyclerSubViews(R.id.profile_followingList, 0, withEffectiveVisibility(Visibility.VISIBLE), R.id.profile_unfollowIcon)
        checkRecyclerSubViews(R.id.profile_followingList, 0, withEffectiveVisibility(Visibility.INVISIBLE), R.id.profile_followingBtn)

        onView(withId(R.id.profile_followingList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickOnViewChild(R.id.profile_unfollowIcon)
            )
        )

        checkRecyclerSubViews(R.id.profile_followingList, 0, withEffectiveVisibility(Visibility.VISIBLE), R.id.profile_followingBtn)
        checkRecyclerSubViews(R.id.profile_followingList, 0, withEffectiveVisibility(Visibility.INVISIBLE), R.id.profile_unfollowIcon)

    }

    @Test
    fun checkFollowBtnClick() {
        val friendsMap: Map<String, Boolean> = mapOf(
            "friend1" to true, "friend2" to true
        )
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",
            12, following = friendsMap)


        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        launchFragmentInHiltContainer<MyProfileFragment>(
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.profile_following)).perform(click())

        onView(withId(R.id.profile_followingList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickOnViewChild(R.id.profile_unfollowIcon)
            )
        )
        onView(withId(R.id.profile_followingList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickOnViewChild(R.id.profile_followingBtn)
            )
        )

        checkRecyclerSubViews(R.id.profile_followingList, 0, withEffectiveVisibility(Visibility.INVISIBLE), R.id.profile_followingBtn)
        checkRecyclerSubViews(R.id.profile_followingList, 0, withEffectiveVisibility(Visibility.VISIBLE), R.id.profile_unfollowIcon)

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
    private fun checkRecyclerSubViews(recyclerViewId: Int, position: Int, itemMatcher: Matcher<View?>, subViewId: Int) {
        onView(withId(recyclerViewId)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
            .check(matches(atPositionOnView(position, itemMatcher, subViewId)))
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

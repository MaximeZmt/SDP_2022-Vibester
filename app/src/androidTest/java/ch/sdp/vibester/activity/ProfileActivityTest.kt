package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.user.User
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileActivityTest {

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
        every { mockAuthenticator.isLoggedIn() } returns false
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
        every {mockUsersRepo.getCurrentUser()} answers {
            null
        }
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
        every { mockUsersRepo.setFieldValue(any(), any(), any()) } answers {}
    }

    val mockImageURI = Uri.parse("https://raw.githubusercontent.com/Ashwinvalento/cartoon-avatar/master/lib/images/male/45.png")
    private fun createMockImageGetter() {
        every {mockImageGetter.fetchImage(any(), any())} answers {
            secondArg<(Uri) -> Unit>().invoke(mockImageURI)
        }
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkProfileData() {
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        onView(withId(R.id.username)).check(matches(withText(inputProfile.username)))
        onView(withId(R.id.profile_total_games_stat)).check(matches(withText(inputProfile.totalGames.toString())))
    }

    @Test
    fun clickScoresShowScores() {
        val scorePerGenre: Map<String, Int> = mapOf(
            "top tracks" to 1,
            "kpop" to 2,
            "rock" to 3,
            "BTS" to 4,
            "Imagine Dragons" to 5,
            "Billie Eilish" to 6)
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",
            12, scores = scorePerGenre)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.profile_scores)).perform(click())

        onView(withId(R.id.profileStatistics)).check(matches(isDisplayed()))

        onView(withId(R.id.profile_top_tracks)).check(matches(withText(inputProfile.scores.getOrDefault("top tracks", 0).toString())))
        onView(withId(R.id.profile_kpop)).check(matches(withText(inputProfile.scores.getOrDefault("kpop", 0).toString())))
        onView(withId(R.id.profile_rock)).check(matches(withText(inputProfile.scores.getOrDefault("rock", 0).toString())))
        onView(withId(R.id.profile_bts)).check(matches(withText(inputProfile.scores.getOrDefault("BTS", 0).toString())))
        onView(withId(R.id.profile_imagine_dragons)).check(matches(withText(inputProfile.scores.getOrDefault("Imagine Dragons", 0).toString())))
        onView(withId(R.id.profile_billie_eilish)).check(matches(withText(inputProfile.scores.getOrDefault("Billie Eilish", 0).toString())))
    }

    @Test
    fun clickBackToMain(){
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.profile_returnToMain)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun shouldShowQrCode() {
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)

        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.showQRCode)).perform(click())

        onView(withId(R.id.QrCodePage)).check(matches(isDisplayed()))
        onView(withId(R.id.profileContent)).check(matches(not(isDisplayed())))
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()))
    }

    @Test
    fun clickBackToProfile() {
        val inputProfile = User("Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.showQRCode)).perform(click())
        onView(withId(R.id.qrCode_returnToProfile)).perform(click())

        onView(withId(R.id.QrCodePage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.profileContent)).check(matches(isDisplayed()))
    }


    @Test
    fun checkEditProfile() {
        val inputProfile = User("Lalisa Bon",R.string.test_profile_image.toString(), "lisa@test.com",  12, 8)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
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
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.editUser)).perform(click())
        onView(withText("Cancel")).perform(click())

        onView(withId(R.id.username)).check(matches(withText("Lalisa Bon")))
    }

    @Test
    fun checkChangePhotoCancel() {
        val inputProfile = User( "Lalisa Bon","bit.ly/3IUnyAF", "lisa@test.com",  12, 8, "VvPB47tQCLdjz3YebilS6h5EXdJ3")
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        //This thread sleep is added for the mock image to load, might be a better way to test it but for now I'll leave it like that
        Thread.sleep(1000)

        onView(withId(R.id.avatar)).perform(click())
        onView(withText("No")).perform(click())
        onView(withId(R.id.avatar)).check(matches(isDisplayed()))
    }

    @Test
    fun checkQrCodeGenerator() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8,"VvPB47tQCLdjz3YebilS6h5EXdJ3")
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.showQRCode)).perform(click())

        onView(withId(R.id.qrCode)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfPictureIsDisplayed() {
        val inputProfile = User( "Lalisa Bon", R.string.test_profile_image.toString(), "lisa@test.com",  12, 8,"VvPB47tQCLdjz3YebilS6h5EXdJ3")
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, ProfileActivity::class.java)

        createMockDataGetter(inputProfile)
        createMockAuthenticator()
        createMockImageGetter()

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)

        Thread.sleep(5000)
        onView(withId(R.id.avatar)).check(matches(isDisplayed()))
    }

}
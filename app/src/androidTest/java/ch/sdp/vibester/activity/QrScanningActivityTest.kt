package ch.sdp.vibester.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
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
class QrScanningActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val mockDataGetter = mockk<DataGetter>()

    private fun createMockDataGetter() {
        val mockUser1 = User("mockUser1", uid = "mockUser1uid")
        val mockUser2 = User("mockUser2", uid = "mockUser2uid")
        val mockUser3 = User("mockUser3", uid = "mockUser3uid")
        val mockUser = User("mockUser", uid = "mockUseruid", following = mapOf(Pair(mockUser2.uid, true), Pair(mockUser3.uid, true)))


        val mockUIDs = arrayListOf<String>("mockUser1uid", "mockUser2uid", "mockUser3uid")

        val mockUsers = arrayListOf<User>(mockUser1, mockUser2, mockUser3)

        every { mockDataGetter.searchByField(any(), any(), any(), any()) } answers  {
            thirdArg<(ArrayList<User>) -> Unit>().invoke(mockUsers)
            lastArg<(ArrayList<String>) -> Unit>().invoke(mockUIDs)
        }
        every {mockDataGetter.setSubFieldValue(any(), any(), any(), any())} answers {}
    }

    @get:Rule(order = 1)
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

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
    fun mainTest() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, QrScanningActivity::class.java)
        intent.putExtra("isTest", true)
        val tempList: ArrayList<String> = ArrayList()
        intent.putExtra("uidList", tempList)
        createMockDataGetter()

        val scn: ActivityScenario<QrScanningActivity> = ActivityScenario.launch(intent)
        Thread.sleep(2000) // needed to execute the function.
        assertThat(scn.result, hasResultCode(Activity.RESULT_CANCELED));
    }


    @Test
    fun testNoExtra() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val intent = Intent(ctx, QrScanningActivity::class.java)
        createMockDataGetter()

        val scn: ActivityScenario<QrScanningActivity> = ActivityScenario.launch(intent)
        assertThat(scn.result, hasResultCode(Activity.RESULT_CANCELED))

    }

}



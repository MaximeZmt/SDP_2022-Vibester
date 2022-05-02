package ch.sdp.vibester.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.sdp.vibester.R
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
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

/*
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class QrScanningActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

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

        val scn: ActivityScenario<ProfileActivity> = ActivityScenario.launch(intent)
        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity::class.java.name))
        //"
        Intents.intended(IntentMatchers.hasExtra("isSuccess", true))
    }



}

 */

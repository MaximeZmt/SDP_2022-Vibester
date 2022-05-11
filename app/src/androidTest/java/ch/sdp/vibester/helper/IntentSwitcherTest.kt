package ch.sdp.vibester.helper

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.GameSetupActivity
import ch.sdp.vibester.activity.WelcomeActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.Serializable


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class IntentSwitcherTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


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
    fun checkIntentOnPlay() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), WelcomeActivity::class.java)
        val scn: ActivityScenario<WelcomeActivity> = ActivityScenario.launch(intent)

        val testMap: HashMap<String, Serializable> = HashMap()
        testMap.put("test", true)

        IntentSwitcher.switch(ApplicationProvider.getApplicationContext(), GameSetupActivity::class.java, testMap)
        Intents.intended(IntentMatchers.hasComponent(GameSetupActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("test", true))
    }

}
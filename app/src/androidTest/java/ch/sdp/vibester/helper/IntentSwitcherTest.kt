package ch.sdp.vibester.helper

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.sdp.vibester.activity.MainActivity
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
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scn: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)

        val testMap: HashMap<String, Serializable> = HashMap()
        testMap.put("test", true)

        IntentSwitcher.switch(ApplicationProvider.getApplicationContext(), MainActivity::class.java, testMap)
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtra("test", true))
    }

}
package ch.sdp.vibester

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class musicTemporaryTest{

        //improve this test to recognize music
        @Test
        fun musicTemporaryTest() {
            val inputName = "Imagine Dragons Believer"
            val intent =
                Intent(ApplicationProvider.getApplicationContext(), musicTemporary::class.java)
            val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)
            Espresso.onView(ViewMatchers.withId(R.id.musicName))
                .perform(ViewActions.typeText(inputName), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.validate)).perform(ViewActions.click())
        }


}
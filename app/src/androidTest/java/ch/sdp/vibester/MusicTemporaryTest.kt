package ch.sdp.vibester

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MusicTemporaryTest{

        // Temporary test
        @Test
        fun musicTemporaryTest() {
            val inputName = "Imagine Dragons Believer"
            val intent =
                Intent(ApplicationProvider.getApplicationContext(), MusicTemporary::class.java)
            val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)
            Espresso.onView(ViewMatchers.withId(R.id.musicName))
                .perform(ViewActions.typeText(inputName), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.validate)).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withId(R.id.textViewPlaying))
                .check(ViewAssertions.matches(ViewMatchers.withText("Imagine Dragons - Believer")))
        }


}
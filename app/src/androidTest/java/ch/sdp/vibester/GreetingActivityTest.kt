package ch.sdp.vibester

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {

    @Test
    fun mainTest(){
        val inputName = "SDPUser"
        val intent = Intent(ApplicationProvider.getApplicationContext(), GreetingActivity::class.java)
        intent.putExtra("name", inputName)
        val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.greetName)).check(matches(withText("Hello $inputName!")))
    }


}
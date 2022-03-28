package ch.sdp.vibester

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
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.helper.GameManager
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EndBasicGameTemporaryTest {

    @get:Rule
    val testRule = ActivityScenarioRule(EndBasicGameTemporary::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkTextView(){
        val gameManager = GameManager()
        lateinit var temp: Unit
        val intent = Intent(ApplicationProvider.getApplicationContext(), TypingGameActivity::class.java)
        intent.putExtra("gameManager", gameManager)
        val scn: ActivityScenario<TypingGameActivity> = ActivityScenario.launch(intent)
        Intents.intended(IntentMatchers.hasComponent(EndBasicGameTemporary::class.java.getName()))
        Espresso.onView(ViewMatchers.withId(R.id.score))
            .check(ViewAssertions.matches(ViewMatchers.withText("Your score is " + gameManager.getScore().toString())))
    }
}
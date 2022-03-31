package ch.sdp.vibester.activity

import android.content.Intent
import android.widget.LinearLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IncorrectSongsActivityTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(IncorrectSongsActivity::class.java)

    private var nbInc = 3
    private var incArray: ArrayList<String> = arrayListOf()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun checkIntentOnCalled() {
        incArray.addAll(arrayOf("One", "Two", "Three", "Four"))
        nbInc = 4

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), IncorrectSongsActivity::class.java)
        intent.putExtra("nb_false", nbInc)
        intent.putStringArrayListExtra("str_arr_inc", incArray)

        val scn: ActivityScenario<IncorrectSongsActivity> = ActivityScenario.launch(intent)

        if (nbInc != 0) {
            lateinit var layout: LinearLayout
            scn.onActivity { activity ->
                layout = activity.findViewById(R.id.incorrect_songs_linear)
            }
            for (x in 0 until nbInc) {
                onView(withId(layout.getChildAt(x).id)).check(matches(withText(incArray[x])))
            }
        } else {
            onView(withId(R.id.incorrect_song_1)).check(matches(withText(R.string.inc_all_correct)))
        }
    }

    @Test
    fun checkIntentOnGoBack() {
        onView(withId(R.id.incorrect_songs_back_to_welcome)).perform(click())
        intended(hasComponent(WelcomeActivity::class.java.name))
    }
}
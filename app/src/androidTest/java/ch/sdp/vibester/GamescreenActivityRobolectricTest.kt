package ch.sdp.vibester

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)

class GamescreenActivityRobolectricTest {

    @Before
    fun setup() {
        val activity = Robolectric.buildActivity(GamescreenActivity::class.java).create().resume().get()
    }

    @Test
    fun buzzerTriggersAlertDialog() {
        Espresso.onView(ViewMatchers.withParent(ViewMatchers.withId(R.id.buzzersLayout)))
            .perform(ViewActions.click())
        assertNotNull(ShadowAlertDialog.getLatestAlertDialog())

    }
}
package ch.sdp.vibester.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LyricsOVHApiInterface

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LyricsBelongGameActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(LyricsBelongGameActivity::class.java)

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun getLyricsFromAPICorrectly() {
        val service = LyricsOVHApiInterface.create()
        val lyric = service.getLyrics("Imagine Dragons", "Thunder").execute()

        assertThat(
            lyric.body().lyrics?.replace("\n", "")?.replace("\r", ""),
            equalTo(("Just a young gun with a quick fuse\n" +
                    "I was uptight, wanna let loose\n" +
                    "I was dreaming of bigger things in\n" +
                    "And wanna leave my own life behind\n" +
                    "Not a yes sir, not a follower\n" +
                    "Fit the box, fit the mold\n" +
                    "Have a seat in the foyer, take a number\n" +
                    "I was lightning before the thunder\n" +
                    "Thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder, thunder\n" +
                    "Thunder\n" +

                    "Kids were laughing in my classes\n" +
                    "While I was scheming for the masses\n" +
                    "Who do you think you are?\n" +
                    "Dreaming 'bout being a big star\n" +
                    "You say you're basic, you say you're easy\n" +
                    "You're always riding in the back seat\n" +
                    "Now I'm smiling from the stage while\n" +
                    "You were clapping in the nose bleeds\n" +

                    "Thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +

                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder\n" +
                    "Thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +

                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +
                    "Thunder, feel the thunder\n" +
                    "Lightning and the thunder, thunder\n" +
                    "Thunder, feel the thunder (feel the)\n" +
                    "Lightning and the thunder, thunder\n" +

                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder\n" +
                    "Thunder, thun-, thunder\n" +
                    "Thun-thun-thunder, thunder").replace("\r", "").replace("\n", ""))
        )
    }

    @Test
    fun btnCheckVisibleAfterSpeak() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testUpdateSpeechResult("hey")
        }
        onView(withId(R.id.lyricMatchResult)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun checkLyricsShouldReturnCorrect() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testCheckLyrics("Just a young gun with a quick fuse")
        }
        Thread.sleep(5000)
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("res: correct")))
    }

    @Test
    fun checkLyricsShouldReturnTooBad() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testCheckLyrics("I don't remember the lyrics")
        }
        Thread.sleep(5000)
        onView(withId(R.id.lyricMatchResult)).check(matches(withText("res: too bad")))
    }

    @Test
    fun shouldUpdateSpeechFromInput() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LyricsBelongGameActivity::class.java)
        val scn: ActivityScenario<LyricsBelongGameActivity> = ActivityScenario.launch(intent)
        scn.onActivity { activity ->
            activity.testUpdateSpeechResult("hey")
        }
        onView(withId(R.id.lyricResult)).check(matches(withText("hey")))
    }


}
package ch.sdp.vibester

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import ch.sdp.vibester.api.LyricsOVHApiInterface
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class LyricTemporaryTest {
    @Test
    fun canGetLyricsFromAPI() {
        val service = LyricsOVHApiInterface.create("https://api.lyrics.ovh/")
        val lyric = service.getLyrics("Imagine Dragons", "Believer").execute()
        assertThat(lyric.body().lyrics, equalTo("First things first\r\n" +
                "I'm say all the words inside my head\r\n" +
                "I'm fired up and tired of the way that things have been, oh-ooh\r\n" +
                "The way that things have been, oh-ooh\r\nSecond thing second\r\n" +
                "Don't you tell me what you think that I can be\n\n" +
                "I'm the one at the sail, I'm the master of my sea, oh-ooh\n\n" +
                "The master of my sea, oh-ooh\n\n\n\nI was broken from a young age\n\n" +
                "Taking my sulkin to the masses\n\nWrite down my poems for the few\n\n" +
                "That looked at me took to me, shook to me, feeling me\n\n" +
                "Singing from heart ache from the pain\n\nTake up my message from the veins\n\n" +
                "Speaking my lesson from the brain\n\nSeeing the beauty through the...\n\n\n\n" +
                "Pain!\n\nYou made me a, you made me a believer, believer\n\nPain!" +
                "\n\nYou break me down, you build me up, believer, believer\n\nPain!\n\n" +
                "I let the bullets fly, oh let them rain\n\nMy life, my love, my drive, it came from...\n\n" +
                "Pain!\n\nYou made me a, you made me a believer, believer\n\n\n\n" +
                "Third things third\n\nSend a prayer to the ones up above\n\n" +
                "All the hate that you've heard has turned your spirit to a dove, oh-ooh\n\n" +
                "Your spirit up above, oh-ooh\n\n\n\nI was choking in the crowd\n\nLiving my brain up in the cloud\n\n" +
                "Falling like ashes to the ground\n\nHoping my feelings, they would drown\n\n" +
                "But they never did, ever lived, ebbing and flowing\n\nInhibited, limited\n\n" +
                "Till it broke up and it rained down\n\nIt rained down, like...\n\n\n\nPain!\n\n" +
                "You made me a, you made me a believer, believer\n\nPain!\n\n" +
                "You break me down, you build me up, believer, believer\n\nPain!\n\n" +
                "I let the bullets fly, oh let them rain\n\nMy life, my love, my drive, they came from...\n\n" +
                "Pain!\n\nYou made me a, you made me a believer, believer\n\n\n\nLast things last\n\n" +
                "By the grace of the fire and the flames\n\nYou're the face of the future, you're the blood in my veins, oh-ooh\n\n" +
                "The blood in my veins, oh-ooh\n\nBut they never did, ever lived, ebbing and flowing\n\nInhibited, limited\n\n" +
                "Till it broke up and it rained down\n\nIt rained down, like...\n\n\n\nPain!" +
                "\n\nYou made me a, you made me a believer, believer\n\nPain!\n\n" +
                "You break me down, you build me up, believer, believer\n\nPain!\n\n" +
                "I let the bullets fly, oh let them rain\n\nMy life, my love, my drive, they came from...\n\n" +
                "Pain!\n\nYou made me a, you made me a believer, believer"))
    }

    @Test
    fun lyricTemporaryTest() {
        val inputArtistName = "Imagine Dragons"
        val inputTrackName = "Believer"
        val intent = Intent(
            ApplicationProvider.getApplicationContext(), LyricTemporary::class.java
        )
        val scn: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.artistForLyric)).perform(typeText(inputArtistName), closeSoftKeyboard())
        onView(withId(R.id.trackForLyric)).perform(typeText(inputTrackName), closeSoftKeyboard())
        onView(withId(R.id.validateForLyric)).perform(click())
        Thread.sleep(1500) // wait for API response
        onView(withId(R.id.lyricBody)).check(matches(withText(containsString("First things first"))))
    }
}
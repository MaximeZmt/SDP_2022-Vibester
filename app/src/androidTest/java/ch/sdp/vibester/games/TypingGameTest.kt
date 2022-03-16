package ch.sdp.vibester.games

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.sdp.vibester.R
import ch.sdp.vibester.model.Song
import okhttp3.internal.wait
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class TypingGameTest{
    @get: Rule
    val activityRule = ActivityScenarioRule(TypingGame::class.java)


    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    /*
    //Weird Test but it's working
    @Test
    fun globalTypingTest(){
        val inputName = "1574210894"
        Espresso.onView(withId(R.id.yourGuessET))
            .perform(ViewActions.typeText(inputName)).perform(closeSoftKeyboard())
        val currenttime = System.currentTimeMillis()
        while(System.currentTimeMillis() < currenttime + 10000){
            //do nothing
        }
        Espresso.onView(withText("Imagine Dragons - Monday")).perform(scrollTo(), click())
        while(System.currentTimeMillis() < currenttime + 1000){
            //do nothing
        }
        Intents.intended(IntentMatchers.toPackage("ch.sdp.vibester"))
        val mysong = Intents.getIntents()[0].extras?.get("song") as Song
        assertEquals("Imagine Dragons", mysong.getArtistName())
        assertEquals("Monday", mysong.getTrackName())
    }

     */

    @Test
    fun borderGenTest(){
        val border = TypingGame.borderGen()
        assertEquals(-0x1, border.color?.defaultColor)
    }

    @Test
    fun spaceGenTest(){
        val height = 10
        val width = 10
        val spaceeeeee = TypingGame.generateSpace(width, height, ApplicationProvider.getApplicationContext())
        assertEquals(width, spaceeeeee.minimumWidth)
        assertEquals(height, spaceeeeee.minimumHeight)
    }

    @Test
    fun textGenTest(){
        val txtInput = "hello"
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val mytest = TypingGame.generateText(txtInput, ctx)
        assertEquals(txtInput, mytest.text.toString())
        assertEquals(200, mytest.minHeight)
        assertEquals(ContextCompat.getColor(ctx, R.color.black), mytest.textColors.defaultColor)
    }

    @Test
    fun imageGenTest(){
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val mySong = Song.singleSong(inputTxt)

        val txtInput = "hello"
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val mytest = TypingGame.generateImage(mySong, ctx)
        assertEquals(200, mytest.minimumHeight)
        assertEquals(200, mytest.minimumWidth)
    }

    @Test
    fun guessLayoutTest(){
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val songTest = Song.singleSong(inputTxt)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val frameLay= TypingGame.guess(songTest, LinearLayout(ctx), ctx)
        frameLay.performClick()
        Intents.intended(IntentMatchers.toPackage("ch.sdp.vibester"))
        val mysong = Intents.getIntents()[0].extras?.get("song") as Song
        assertEquals(songTest.getArtistName(), mysong.getArtistName())
        assertEquals(songTest.getTrackName(), mysong.getTrackName())
    }

}
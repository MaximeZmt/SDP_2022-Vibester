package ch.sdp.vibester.activity

import android.content.Context
import android.media.MediaPlayer
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.games.GameManager
import ch.sdp.vibester.model.Song
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CompletableFuture

class TypingGameActivityTest{
    private val BY_TAG = "tag.gettoptracks"

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun borderGenTest(){
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val border = TypingGameActivity().borderGen(ctx)
        assertEquals(getColor(ctx, R.color.maximum_yellow_red), border.color?.defaultColor)
    }

    @Test
    fun spaceGenTest(){
        val height = 10
        val width = 10
        val spaceeeeee = TypingGameActivity().generateSpace(width, height, ApplicationProvider.getApplicationContext())
        assertEquals(width, spaceeeeee.minimumWidth)
        assertEquals(height, spaceeeeee.minimumHeight)
    }

    @Test
    fun textGenTest(){
        val txtInput = "hello"
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val mytest = TypingGameActivity().generateText(txtInput, ctx)
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
        val mytest = TypingGameActivity().generateImage(mySong, ctx)
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
        val managerTxt = """
            {"tracks":
            {"track":[{"name":"Wonderwall","duration":"259","mbid":"31623cce-9717-4513-9d83-1b5d04e44f9b",
            "url":"https://www.last.fm/music/Oasis/_/Wonderwall",
            "streamable":{"#text":"0","fulltrack":"0"},
            "artist":{"name":"Oasis","mbid":"ecf9f3a3-35e9-4c58-acaa-e707fba45060","url":"https://www.last.fm/music/Oasis"},
            "image":[{"#text":"https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"small"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"medium"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"large"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png","size":"extralarge"}],
            "@attr":{"rank":"1"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
            """
        val songTest = Song.singleSong(inputTxt)
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val cfmp = CompletableFuture<MediaPlayer>()
        val gameManager = GameManager();
        gameManager.setGameSongList(managerTxt,BY_TAG)
        gameManager.setNextSong()
        cfmp.complete(MediaPlayer())
        val frameLay= TypingGameActivity().guess(songTest, LinearLayout(ctx), ctx, gameManager)
        frameLay.performClick()
//        Intents.intended(IntentMatchers.toPackage("ch.sdp.vibester"))
//        val mysong = Intents.getIntents()[0].extras?.get("song") as Song
        assertEquals(songTest.getArtistName(), gameManager.getCurrentSong().getArtistName())
        assertEquals(songTest.getTrackName(), gameManager.getCurrentSong().getTrackName())
    }

    @Test
    fun intentGenWrongTest(){
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """
        val managerTxt = """
            {"tracks":
            {"track":[{"name":"Wonderwall","duration":"259","mbid":"31623cce-9717-4513-9d83-1b5d04e44f9b",
            "url":"https://www.last.fm/music/Oasis/_/Wonderwall",
            "streamable":{"#text":"0","fulltrack":"0"},
            "artist":{"name":"Oasis","mbid":"ecf9f3a3-35e9-4c58-acaa-e707fba45060","url":"https://www.last.fm/music/Oasis"},
            "image":[{"#text":"https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"small"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"medium"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"large"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png","size":"extralarge"}],
            "@attr":{"rank":"1"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
            """

        val songTest = Song.singleSong(inputTxt)
        val gameManager = GameManager()
        gameManager.setGameSongList(managerTxt,BY_TAG)
        gameManager.setNextSong()
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val myIntent = TypingGameActivity().checkAnswer(ctx, songTest, gameManager)
        assertEquals(true, gameManager.getScore()==0)
    }


}
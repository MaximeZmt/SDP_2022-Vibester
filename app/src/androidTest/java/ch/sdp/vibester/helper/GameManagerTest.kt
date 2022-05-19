package ch.sdp.vibester.helper

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.api.LastfmMethod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class GameManagerTest {
    private fun setGameManager(valid:Boolean = true): GameManager {
        var managerTxt = ""
        if (valid) {
             managerTxt = """
                {"tracks":
                {"track":[{"name":"Monday","duration":"259",
                "artist":{"name":"Imagine Dragons"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
                """
        } else {
             managerTxt = """
                {"tracks":
                {"track":[{"name":"TEST_MUSIC_TEST","duration":"259",
                "artist":{"name":"TEST_ARTIST_TEST"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
                """
        }
        val gameManager = GameManager()
        gameManager.setGameSongList(managerTxt, LastfmMethod.BY_TAG.method)
        return gameManager
    }

    @Test
    fun getGameSongListCorrect() {
        val gameManager = setGameManager()
        assertEquals(gameManager.getSongList()[0], Pair("monday", "imagine dragons"))
    }

    @Test
    fun setGameSizeCorrect() {
        val gameManager = setGameManager()
        gameManager.setGameSize(3)
        assertEquals(gameManager.gameSize, 3)
    }

    @Test
    fun setNextSongError(){
        val gameManager = setGameManager(false)
        assertEquals(gameManager.setNextSong(), false)
    }

    @Test
    fun getScoreCorrect() {
        val gameManager = setGameManager()
        assertEquals(gameManager.getScore(), 0)
    }

    @Test
    fun getPlayedSongsCorrect() {
        val gameManager = setGameManager()
        assertEquals(gameManager.getPlayedSongsCount(), 0)
    }

    @Test
    fun addWrongSongCorrect() {
        val gameManager = setGameManager()
        gameManager.setNextSong()
        gameManager.addWrongSong()
        assertEquals(gameManager.getWrongSongs()[0].getArtistName().lowercase(), "imagine dragons")
        assertEquals(gameManager.getWrongSongs()[0].getTrackName().lowercase(), "monday")
    }

    @Test
    fun addCorrectSongCorrect() {
        val gameManager = setGameManager()
        gameManager.setNextSong()
        gameManager.addCorrectSong()
        assertEquals(
            gameManager.getCorrectSongs()[0].getArtistName().lowercase(),
            "imagine dragons"
        )
        assertEquals(gameManager.getCorrectSongs()[0].getTrackName().lowercase(), "monday")
    }

    @Test
    fun setNextSongCorrect() {
        val gameManager = setGameManager()
        val ret = gameManager.setNextSong()
        assertEquals(ret, true)
        assertEquals(gameManager.getCurrentSong().getArtistName().lowercase(), "imagine dragons")
        assertEquals(gameManager.getCurrentSong().getTrackName().lowercase(), "monday")
    }

    //OFFLINE TESTS

    private fun offlineTestSetup(): GameManager {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("Song 1 - Artist 1\n")
        records.appendText("Song 2 - Artist 2\n")

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Artwork 1 - Preview 1\n")
        properties.appendText("Song 2 - Artist 2 - Artwork 2 - Preview 2\n")

        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        testing1.createNewFile()
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")
        testing2.createNewFile()

        lateinit var manager: GameManager
        val path = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if(path != null) {
            manager = createOfflineGameManager(path)
        }
        return manager
    }

    private fun createOfflineGameManager(path: File): GameManager {
        val gameManager = GameManager()
        if(path != null) {
            gameManager.setOffline(path, false)
        }
        gameManager.setGameSongList("", LastfmMethod.BY_TAG.method)
        return gameManager
    }

    @Test
    fun checkExistsError() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val gameManager = offlineTestSetup()

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        if(properties.exists()){
            properties.delete()
        }

        assert(!gameManager.setNextSong())

        offlineTestCleanup()
    }

    @Test
    fun checkEmptyLineError() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val gameManager = offlineTestSetup()

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        if(properties.exists()) {
            properties.delete()
        }
        properties.createNewFile()
        properties.appendText("\n")

        assert(!gameManager.setNextSong())

        offlineTestCleanup()
    }

    @Test
    fun checkIncorrectInformationInPropertiesShort() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val gameManager = offlineTestSetup()

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        if(properties.exists()){
            properties.delete()
        }
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Preview 1\n")

        assert(!gameManager.setNextSong())

        offlineTestCleanup()
    }

    @Test
    fun checkIncorrectInformationInPropertiesLong() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val gameManager = offlineTestSetup()

        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        if(properties.exists()){
            properties.delete()
        }
        properties.createNewFile()
        properties.appendText("Song 1 - Artist 1 - Preview 1 - Artwork 1 - Extra 1\n")

        assert(!gameManager.setNextSong())

        offlineTestCleanup()
    }

    @Test
    fun checkCurrentSong() {
        val gameManager = offlineTestSetup()

        assert(gameManager.setNextSong())

        val curSong = gameManager.currentSong
        assertEquals(curSong.getTrackName(), "Song 1")
        assertEquals(curSong.getArtistName(), "Artist 1")
        assertEquals(curSong.getPreviewUrl(), "Preview 1")
        assertEquals(curSong.getArtworkUrl(), "Artwork 1")


        offlineTestCleanup()
    }

    private fun offlineTestCleanup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        var properties = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "properties.txt")
        var testing1 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 1 - Artist 1")
        var testing2 = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "extract_of_Song 2 - Artist 2")

        records.delete()
        properties.delete()
        testing1.delete()
        testing2.delete()
    }
}
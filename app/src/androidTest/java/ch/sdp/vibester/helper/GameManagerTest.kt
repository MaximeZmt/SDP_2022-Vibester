package ch.sdp.vibester.helper

import ch.sdp.vibester.api.LastfmMethod
import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun increaseScoreCorrect() {
        val gameManager = setGameManager()
        gameManager.increaseScore()
        assertEquals(gameManager.getScore(), 1)
        gameManager.increaseScore()
        assertEquals(gameManager.getScore(), 2)
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
}
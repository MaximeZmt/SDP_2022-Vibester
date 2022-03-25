package ch.sdp.vibester.helper

import org.junit.Assert.assertEquals
import org.junit.Test

class GameManagerTest {
    private val BY_TAG = "tag.gettoptracks"
    private fun setGameManager(): GameManager {
            val managerTxt = """
                {"tracks":
                {"track":[{"name":"Monday","duration":"259","mbid":"31623cce-9717-4513-9d83-1b5d04e44f9b",
                "url":"https://www.last.fm/music/Oasis/_/Wonderwall",
                "streamable":{"#text":"0","fulltrack":"0"},
                "artist":{"name":"Imagine Dragons","mbid":"ecf9f3a3-35e9-4c58-acaa-e707fba45060","url":"https://www.last.fm/music/Oasis"},
                "image":[{"#text":"https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"small"},
                {"#text":"https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"medium"},
                {"#text":"https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"large"},
                {"#text":"https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png","size":"extralarge"}],
                "@attr":{"rank":"1"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
                """
            val gameManager = GameManager();
            gameManager.setGameSongList(managerTxt, BY_TAG)
            return gameManager
        }

    @Test
    fun getGameSongListCorrect(){
        val gameManager = setGameManager()
        assertEquals(gameManager.getSongList()[0],"monday imagine dragons")
    }

    @Test
    fun getScoreCorrect(){
        val gameManager = setGameManager()
        assertEquals(gameManager.getScore(),0)
    }

    @Test
    fun increaseScoreCorrect(){
        val gameManager = setGameManager()
        gameManager.increaseScore()
        assertEquals(gameManager.getScore(),1)
        gameManager.increaseScore()
        assertEquals(gameManager.getScore(),2)
    }

    @Test
    fun getPlayedSongsCorrect(){
        val gameManager = setGameManager()
        assertEquals(gameManager.getPlayedSongs(),0)
    }

    @Test
    fun addWrongSongCorrect(){
        val gameManager = setGameManager()
        gameManager.setNextSong()
        gameManager.addWrongSong()
        assertEquals(gameManager.getWrongSongs()[0].getArtistName().lowercase(), "imagine dragons")
        assertEquals(gameManager.getWrongSongs()[0].getTrackName().lowercase(), "monday")
    }

    @Test
    fun addCorrectSongCorrect(){
        val gameManager = setGameManager()
        gameManager.setNextSong()
        gameManager.addCorrectSong()
        assertEquals(gameManager.getCorrectSongs()[0].getArtistName().lowercase(), "imagine dragons")
        assertEquals(gameManager.getCorrectSongs()[0].getTrackName().lowercase(), "monday")
    }

    @Test
    fun setNextSongCorrect(){
        val gameManager = setGameManager()
        val ret = gameManager.setNextSong()
        assertEquals(ret, true)
        assertEquals(gameManager.getCurrentSong().getArtistName().lowercase(), "imagine dragons")
        assertEquals(gameManager.getCurrentSong().getTrackName().lowercase(), "monday")
    }
}
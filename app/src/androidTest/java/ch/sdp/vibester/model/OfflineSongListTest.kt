package ch.sdp.vibester.model

import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.api.LastfmMethod
import org.junit.Assert
import org.junit.Test

class OfflineSongListTest {

    @Test
    fun noSongsAvailableInitialization() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val mySongsList = OfflineSongList(context, LastfmMethod.BY_TAG.method)

        val inputSongsList = mutableListOf<Pair<String, String>>()
        val page = "1"
        val songsPerPage = "100"
        val totalPages = "20"
        val totalSongs = "2000"


        Assert.assertEquals(inputSongsList, mySongsList.getSongList())
        Assert.assertEquals(page, mySongsList.getPage())
        Assert.assertEquals(songsPerPage, mySongsList.getSongsPerPage())
        Assert.assertEquals(totalPages, mySongsList.getTotalPages())
        Assert.assertEquals(totalSongs, mySongsList.getTotalSongs())
        Assert.assertTrue(mySongsList.getEmptySongs())
    }
}
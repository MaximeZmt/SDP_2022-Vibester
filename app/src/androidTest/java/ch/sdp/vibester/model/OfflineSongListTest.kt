package ch.sdp.vibester.model

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.api.LastfmMethod
import org.junit.Assert
import org.junit.Test
import java.io.File

class OfflineSongListTest {

    @Test
    fun noSongsAvailableInitialization() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val mySongsList = OfflineSongList(context)

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

    @Test
    fun addSpecificSongToList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("bones - imagine dragons\n")

        val mySongsList = OfflineSongList(context)

        val songName = "bones"
        val artistName = "imagine dragons"
        val inputSongsList = mutableListOf(Pair(songName, artistName))
        val page = "1"
        val songsPerPage = "100"
        val totalPages = "20"
        val totalSongs = "2000"

        Assert.assertEquals(inputSongsList, mySongsList.getSongList())
        Assert.assertEquals(page, mySongsList.getPage())
        Assert.assertEquals(songsPerPage, mySongsList.getSongsPerPage())
        Assert.assertEquals(totalPages, mySongsList.getTotalPages())
        Assert.assertEquals(totalSongs, mySongsList.getTotalSongs())
        Assert.assertTrue(!mySongsList.getEmptySongs())

        records.delete()
    }
}
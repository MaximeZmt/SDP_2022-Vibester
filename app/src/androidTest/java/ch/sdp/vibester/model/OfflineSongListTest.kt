package ch.sdp.vibester.model

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import java.io.File

class OfflineSongListTest {

    @Test
    fun noSongsAvailableInitialization() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        lateinit var mySongsList: OfflineSongList
        if (path != null) {
            mySongsList = OfflineSongList(path)
        }

        val inputSongsList = mutableListOf<Pair<String, String>>()
        val page = "1"
        val songsPerPage = "100"
        val totalPages = "20"
        val totalSongs = "2000"

        Assert.assertEquals(inputSongsList, mySongsList.songList)
        Assert.assertEquals(page, mySongsList.getPage())
        Assert.assertEquals(songsPerPage, mySongsList.getSongsPerPage())
        Assert.assertEquals(totalPages, mySongsList.getTotalPages())
        Assert.assertEquals(totalSongs, mySongsList.getTotalSongs())
        Assert.assertTrue(mySongsList.getEmptySongs())
    }

    @Test
    fun addSpecificSongToList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")
        records.createNewFile()
        records.appendText("bones - imagine dragons\n")

        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        lateinit var mySongsList: OfflineSongList
        if (path != null) {
            mySongsList = OfflineSongList(path)
        }

        val songName = "bones"
        val artistName = "imagine dragons"
        val inputSongsList = mutableListOf(Pair(songName, artistName))
        val page = "1"
        val songsPerPage = "100"
        val totalPages = "20"
        val totalSongs = "2000"

        Assert.assertEquals(inputSongsList, mySongsList.songList)
        Assert.assertEquals(page, mySongsList.getPage())
        Assert.assertEquals(songsPerPage, mySongsList.getSongsPerPage())
        Assert.assertEquals(totalPages, mySongsList.getTotalPages())
        Assert.assertEquals(totalSongs, mySongsList.getTotalSongs())
        Assert.assertTrue(!mySongsList.getEmptySongs())

        records.delete()
    }
}
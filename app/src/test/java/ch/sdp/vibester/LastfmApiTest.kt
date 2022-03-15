package ch.sdp.vibester

import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.api.LastfmApi
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongsList
import junit.framework.Assert.assertTrue
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.Exception

class LastfmApiTest {
//    @Test
//    fun lastfmApiQueryWorks() {
//        var songsListFut = LastfmApi.querySongsByTag(OkHttpClient(), "rock")
//        val songsListObj = SongsList(songsListFut.get())
//        val songsPerPage = songsListObj.getSongsPerPage().toInt()
//        val songsList = songsListObj.getSongs()
//        assertTrue(songsList.size > 0)
//    }

    @get:Rule
    var exception = ExpectedException.none()

    @Test
    fun songsByTagError() {
        exception.expect(Exception::class.java)
        var songsFut = LastfmApi.querySongsByTag(OkHttpClient(), tag="rock", baseUrl="ThisSiteDoesNotExist" )
        songsFut.get()
    }

    @Test
    fun songsByChartError() {
        exception.expect(Exception::class.java)
        var songsFut = LastfmApi.querySongsByChart(OkHttpClient(), baseUrl="ThisSiteDoesNotExist" )
        songsFut.get()
    }
}
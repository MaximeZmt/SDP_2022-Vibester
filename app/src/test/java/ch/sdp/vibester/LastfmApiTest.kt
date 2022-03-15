package ch.sdp.vibester

import ch.sdp.vibester.api.LastfmApi
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.model.SongsList
import junit.framework.Assert.assertTrue
import okhttp3.OkHttpClient
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.lang.Exception

class LastfmApiTest {
    private val BY_TAG = "tag.gettoptracks"
    private val BY_CHART = "chart.gettoptracks"

//    @Test
//    fun lastfmApiQueryWorks() {
//        var songsListFut = LastfmApi.querySongsList(OkHttpClient(), LastfmUri(method = BY_TAG, tag="rock"))
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
        var songsFut = LastfmApi.querySongsList(OkHttpClient(), LastfmUri(method = BY_TAG, tag="rock"), baseUrl="ThisSiteDoesNotExist" )
        songsFut.get()
    }

}
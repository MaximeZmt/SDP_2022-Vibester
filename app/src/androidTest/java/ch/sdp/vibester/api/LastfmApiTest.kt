package ch.sdp.vibester.api

import ch.sdp.vibester.model.SongList
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import okhttp3.OkHttpClient
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.lang.Exception

class LastfmApiTest {
    private val BY_TAG = "tag.gettoptracks"

    @Test
    fun lastfmApiQueryWorks() {
        var songListFut = LastfmApi.querySongList(OkHttpClient(), LastfmUri(method = BY_TAG, tag="rock"))
        val songListObj = SongList(songListFut.get(), BY_TAG)
        assertTrue(songListObj.getSongList().size > 0)
//        assertTrue(songListObj.getSongList().size == songListObj.getSongsPerPage().toInt())
//        assertEquals(songListObj.getPage(),"1")
    }

    @get:Rule
    var exception = ExpectedException.none()

    @Test
    fun lastfmApiQueryError() {
        exception.expect(Exception::class.java)
        var songsFut = LastfmApi.querySongList(OkHttpClient(), LastfmUri(method = BY_TAG, tag="rock"), baseUrl="ThisSiteDoesNotExist" )
        songsFut.get()
    }

}
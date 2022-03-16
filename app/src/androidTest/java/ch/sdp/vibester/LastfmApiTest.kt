package ch.sdp.vibester

import ch.sdp.vibester.api.LastfmApi
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.model.SongList
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
        val songListObj = SongList(songListFut.get())
        assertTrue(songListObj.getSongList().size > 0)
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
package ch.sdp.vibester.api



import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.Exception


class ItunesMusicApiTest{

    @Test
    fun itunesAPIQueryWorks() {
        var songFut = ItunesMusicApi.querySong("imagine dragons believer", OkHttpClient(), 1)
        val song = Song.singleSong(songFut.get())
        assertEquals("Imagine Dragons", song.getArtistName())
    }

    @get:Rule
    var exception = ExpectedException.none()

    @Test
    fun itunesAPIQueryError() {
        exception.expect(Exception::class.java)
        var songFut = ItunesMusicApi.querySong("imagine dragons believer", OkHttpClient(), 1,"https://ThisIsNotAnURL666.notADomain")
        songFut.get()
    }

    @Test
    fun itunesAPIQueryWorksComplete() {
        var songFut = ItunesMusicApi.querySong("imagine dragons believer", OkHttpClient(), 1)
        val song = Song.singleSong(songFut.get())
        val mediaFut = AudioPlayer.playAudio(song.getPreviewUrl())
        val player = mediaFut.get()
        assertEquals(true, player.isPlaying)
    }

    @Test
    fun itunesAPIQueryCompleteError() {
        exception.expect(Exception::class.java)
        val mediaFut = AudioPlayer.playAudio("https://ThisIsNotAnURL666.notADomain")
        mediaFut.get()
    }


}

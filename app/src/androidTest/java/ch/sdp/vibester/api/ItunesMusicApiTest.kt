package ch.sdp.vibester.api


import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


class ItunesMusicApiTest {

    @Test
    fun itunesAPIQueryWorks() {
        val songFut = ItunesMusicApi.querySong("imagine dragons believer", OkHttpClient(), 1)
        val song = Song.singleSong(songFut.get())
        assertEquals("Imagine Dragons", song.getArtistName())
    }

    @get:Rule
    var exception: ExpectedException = ExpectedException.none()

    @Test
    fun itunesAPIQueryError() {
        exception.expect(Exception::class.java)
        val songFut = ItunesMusicApi.querySong(
            "imagine dragons believer",
            OkHttpClient(),
            1,
            "https://ThisIsNotAnURL666.notADomain"
        )
        songFut.get()
    }

//    @Test
//    fun itunesAPIQueryWorksComplete() {
//        val songFut = ItunesMusicApi.querySong("imagine dragons believer", OkHttpClient(), 1)
//        val song = Song.singleSong(songFut.get())
//        val mediaFut = AudioPlayer.playAudio(song.getPreviewUrl())
//        val player = mediaFut.get()
//        assertEquals(true, player.isPlaying)
//    }

    @Test
    fun itunesAPIQueryCompleteError() {
        exception.expect(Exception::class.java)
        val mediaFut = AudioPlayer.playAudio("https://ThisIsNotAnURL666.notADomain")
        mediaFut.get()
    }


}

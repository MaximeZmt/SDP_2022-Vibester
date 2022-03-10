package ch.sdp.vibester.api

import org.junit.Assert.assertEquals
//import org.mockito.Mockito.times
//import org.mockito.Mockito.verify


import android.media.MediaPlayer
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.IOException
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;


class ItunesMusicApiTest{

    //@Mock
    //var eventListener: onPreparedListener? = null

    @get:Rule
    var exception = ExpectedException.none()

    @Test
    fun itunesMediaPlayAudioError() {
        exception.expect(IllegalArgumentException::class.java)
        ItunesMusicApi.playAudio("")
    }

    @Test
    fun itunesMediaPlayAudio() {
        var mediaPlayer = ItunesMusicApi.playAudio("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a")
        mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            mediaPlayer.start()
            assertEquals(true, mediaPlayer.isPlaying)
        })

    }


}

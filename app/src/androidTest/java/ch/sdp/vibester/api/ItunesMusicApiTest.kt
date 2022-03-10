package ch.sdp.vibester.api

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.sdp.vibester.MainActivity
import ch.sdp.vibester.model.Song
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.concurrent.thread



class ItunesMusicApiTest{

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
            if(mediaPlayer.isPlaying){
                throw IOException()
            }
            //assertEquals(false, mediaPlayer.isPlaying)
        })

    }


}

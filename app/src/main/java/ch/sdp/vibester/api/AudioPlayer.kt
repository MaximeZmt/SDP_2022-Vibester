package ch.sdp.vibester.api

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import java.io.IOException
import java.util.concurrent.CompletableFuture

class AudioPlayer private constructor() {
    companion object {
        /**
         * A function that given an audio stream url will play it
         * @param audioUrl Url in String pointing towards the audio stream OR the path to the file to be listened to
         * @return CompletableFuture<MediaPlayer> that contains the current playing mediaPlayer
         */
        fun playAudio(audioUrl: String): CompletableFuture<MediaPlayer> {
            val mediaFut = CompletableFuture<MediaPlayer>()
            var mediaPlayer: MediaPlayer = MediaPlayer() // don't remove the Type
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            try {
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepare()
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                    mediaFut.complete(mediaPlayer)
                }
            } catch (e: IOException) {
                Log.e("[PlayAudio]", "Error see stacktrace")
                mediaFut.completeExceptionally(e)
            }
            return mediaFut
        }
    }
}
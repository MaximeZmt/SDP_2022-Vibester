package ch.sdp.vibester.api

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 * Static class to access web API of Itunes
 */
class ItunesMusicApi private constructor(){

    companion object{
        private val LOOKUP_URL_BASE = "https://itunes.apple.com/search?limit=1&term="

        /**
         * Given a String query it will provide the music url preview
         * @param query String containing the request to the API
         * @param okHttp An OkHttpClient
         * @param baseUrl (Optional) If you want to specify an other url to query
         * @return CompletableFuture<String> that contains the result of the query
         */
        fun querySong(query: String, okHttp: OkHttpClient, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
            val buildedUrl = baseUrl+query.replace(' ', '+')
            val req = okhttp3.Request.Builder().url(buildedUrl).build()

            var retFuture = CompletableFuture<String>()

            okHttp.newCall(req).enqueue(ApiCallback(retFuture))

            return retFuture
        }

        /**
         * A function that given an audio stream url will play it
         * @param audioUrl Url in String pointing towards the audio stream
         * @return CompletableFuture<MediaPlayer> that contains the current playing mediaPlayer
         */
        fun playAudio(audioUrl: String): CompletableFuture<MediaPlayer>{
            val mediaFut = CompletableFuture<MediaPlayer>()
            var mediaPlayer: MediaPlayer = MediaPlayer()
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
            }catch (e: IOException){
                Log.e("[PlayAudio]", "Error see stacktrace")
                mediaFut.completeExceptionally(e)
            }
            return mediaFut
        }
        
    }


}
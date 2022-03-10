package ch.sdp.vibester.api

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import ch.sdp.vibester.model.Song
//import com.android.volley.Request
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.concurrent.CompletableFuture

class ItunesMusicApi private constructor(){

    companion object{
        private val LOOKUP_URL_BASE = "https://itunes.apple.com/search?limit=1&term="

        /**
         * Given a String query it will provide the music url preview
         */
        fun querySong(query: String, okHttp: OkHttpClient, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
            val buildedUrl = baseUrl+query.replace(' ', '+')
            val req = okhttp3.Request.Builder().url(buildedUrl).build()

            var retFuture = CompletableFuture<String>()

            okHttp.newCall(req).enqueue(SongCallback(retFuture))

            return retFuture
        }

        private class SongCallback(val retFuture: CompletableFuture<String>): Callback{
            override fun onResponse(call: Call, response: Response) {
                retFuture.complete(response.body?.string())
            }

            override fun onFailure(call: Call, e: IOException) {
                retFuture.completeExceptionally(e)
            }
        }

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
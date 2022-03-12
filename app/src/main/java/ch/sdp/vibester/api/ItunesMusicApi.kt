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
        private val LOOKUP_URL_BASE = "https://itunes.apple.com/search?media=music&"

        /**
         * Given a String query it will provide the music url preview
         * @param query String containing the request to the API
         * @param okHttp An OkHttpClient
         * @param baseUrl (Optional) If you want to specify an other url to query
         * @return CompletableFuture<String> that contains the result of the query
         */
        fun querySong(query: String, okHttp: OkHttpClient, limit: Int, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
            val buildedUrl = baseUrl+"limit="+limit+"&term="+query.replace(' ', '+')
            val req = okhttp3.Request.Builder().url(buildedUrl).build()

            var retFuture = CompletableFuture<String>()

            okHttp.newCall(req).enqueue(SongCallback(retFuture))

            return retFuture
        }


        /**
         * The Callback class when calling the querySong(...) method
         */
        private class SongCallback(val retFuture: CompletableFuture<String>): Callback{
            override fun onResponse(call: Call, response: Response) {
                retFuture.complete(response.body?.string())
            }

            override fun onFailure(call: Call, e: IOException) {
                retFuture.completeExceptionally(e)
            }
        }


    }


}
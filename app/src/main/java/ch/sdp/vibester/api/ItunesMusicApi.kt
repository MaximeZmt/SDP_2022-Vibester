package ch.sdp.vibester.api

import okhttp3.OkHttpClient
import java.util.concurrent.CompletableFuture

/**
 * Static class to access web API of Itunes
 */
class ItunesMusicApi private constructor(){

    companion object{
        private const val LOOKUP_URL_BASE = "https://itunes.apple.com/search?media=music&"

        /**
         * Given a String query it will provide the music url preview
         * @param query String containing the request to the API
         * @param okHttp An OkHttpClient
         * @param baseUrl (Optional) If you want to specify an other url to query
         * @return CompletableFuture<String> that contains the result of the query
         */
        fun querySong(query: String, okHttp: OkHttpClient, limit: Int, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
            val builtUrl = baseUrl+"limit="+limit+"&term="+query.replace(' ', '+')
            val req = okhttp3.Request.Builder().url(builtUrl).build()

            val retFuture = CompletableFuture<String>()

            okHttp.newCall(req).enqueue(ApiCallback(retFuture))

            return retFuture
        }

    }


}
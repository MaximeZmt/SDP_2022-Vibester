package ch.sdp.vibester.api

import android.net.Uri
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CompletableFuture


/**
 * Class to retrieve list of songs by tag from LastFm
 */
class LastfmApi private constructor(){

        companion object{
            private val API_KEY = "52bfdc690dd8373bba5351571a01ac14"
            private val LOOKUP_URL_BASE ="https://ws.audioscrobbler.com/2.0/"
            private val BY_TAG = "tag.gettoptracks"
            private val BY_CHART = "chart.gettoptracks"

            /**
             * Provide a list of songs by given tag (String query)
             */
            fun querySongsByTag(okHttp: OkHttpClient, tag: String, page:Int = 1, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
                var builtUri: Uri= Uri.parse(baseUrl)
                    .buildUpon()
                    .appendQueryParameter("method", BY_TAG)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format","json")
                    .appendQueryParameter("page", page.toString())
                    .appendQueryParameter("tag", tag)
                    .build()

                val uri = builtUri.toString()
                val req = Request.Builder().url(uri).build()

                var retFuture = CompletableFuture<String>()

                okHttp.newCall(req).enqueue(SongsByTagCallback(retFuture))

                return retFuture
            }

            /**
             * Provide a list of songs by chart (String query)
             */
            fun querySongsByChart(okHttp: OkHttpClient, page:Int = 1, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
                var builtUri: Uri= Uri.parse(baseUrl)
                    .buildUpon()
                    .appendQueryParameter("method", BY_CHART)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format","json")
                    .appendQueryParameter("page", page.toString())
                    .build()
                val uri = builtUri.toString()
                val req = Request.Builder().url(uri).build()

                var retFuture = CompletableFuture<String>()

                okHttp.newCall(req).enqueue(SongsByTagCallback(retFuture))

                return retFuture
            }


            /**
             * Callback class when calling the querySongsBtTag method
             */
            private class SongsByTagCallback(val retFuture: CompletableFuture<String>): Callback {
                override fun onResponse(call: Call, response: Response) {
                    retFuture.complete(response.body?.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    retFuture.completeExceptionally(e)
                }
            }
        }



}
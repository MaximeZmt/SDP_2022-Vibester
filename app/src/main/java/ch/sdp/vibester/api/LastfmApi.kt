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

            /**
             * Provide a list of songs by given tag (String query)
             */
            fun querySongList(okHttp: OkHttpClient, params:LastfmUri, baseUrl: String = LOOKUP_URL_BASE): CompletableFuture<String> {
                var builtUri: Uri= Uri.parse(baseUrl)
                    .buildUpon()
                    .appendQueryParameter("method", params.method)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format",params.format)
                    .appendQueryParameter("page", params.page)
                    .appendQueryParameter("tag", params.tag)
                    .build()

                val uri = builtUri.toString()
                val req = Request.Builder().url(uri).build()

                var retFuture = CompletableFuture<String>()

                okHttp.newCall(req).enqueue(ApiCallback(retFuture))

                return retFuture
            }

        }



}
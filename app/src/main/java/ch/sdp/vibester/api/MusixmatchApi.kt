package ch.sdp.vibester.api

import okhttp3.OkHttpClient
import java.util.concurrent.CompletableFuture

class MusixmatchApi private constructor(){

    companion object {
        // musixmatch api base url
        private const val BASE_URL = "https://api.musixmatch.com/ws/1.1/"

        private const val API_KEY = "&apikey=c0c85b22538a6b16bdfbbf64b4078c"

        private const val FORMAT_URL = "?format=json&callback=callback"

        private const val LYRICS_MATCHER = "matcher.lyrics.get"

        private const val ARTIST_SEARCH_PARAMETER = "&q_artist="

        private const val TRACK_SEARCH_PARAMETER = "&q_track="

        fun queryLyric(artistName: String, trackName: String, okHttpClient: OkHttpClient): CompletableFuture<String> {
            val apiCall = BASE_URL + LYRICS_MATCHER + FORMAT_URL + ARTIST_SEARCH_PARAMETER + artistName + TRACK_SEARCH_PARAMETER + trackName + API_KEY

            val request = okhttp3.Request.Builder().url(apiCall).build()

            var retFuture = CompletableFuture<String>()

            okHttpClient.newCall(request).enqueue(ItunesMusicApi.Companion.SongCallback(retFuture))

            return retFuture
        }
    }
}
package ch.sdp.vibester.api

import ch.sdp.vibester.model.Lyric
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsOVHApiInterface {
    @GET("v1/{artist}/{title}")
    fun getLyrics(@Path("artist") artist: String, @Path("title") title: String): Call<Lyric>
}
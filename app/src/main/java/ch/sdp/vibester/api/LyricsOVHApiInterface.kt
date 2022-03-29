package ch.sdp.vibester.api

import ch.sdp.vibester.model.Lyric
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsOVHApiInterface {
    /**
     * Given the name of the artist and the title of the track
     * return a Call<Lyric> contains the lyric of the track
     */
    @GET("https://api.lyrics.ovh/v1/{artist}/{title}")
    fun getLyrics(@Path("artist") artist: String, @Path("title") title: String): Call<Lyric>
}
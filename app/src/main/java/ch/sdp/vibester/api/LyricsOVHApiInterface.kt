package ch.sdp.vibester.api

import ch.sdp.vibester.model.Lyric
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.lyrics.ovh/"

interface LyricsOVHApiInterface {
    /**
     * Given the name of the artist and the title of the track
     * return a Call<Lyric> contains the lyric of the track
     */
    @GET("v1/{artist}/{title}")
    fun getLyrics(@Path("artist") artist: String, @Path("title") title: String): Call<Lyric>

    companion object {
        /**
         * @param baseUrl base url of the api
         * return an instance of the api interface
         */
        fun create(baseUrl:String = BASE_URL): LyricsOVHApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(LyricsOVHApiInterface::class.java)
        }
    }
}
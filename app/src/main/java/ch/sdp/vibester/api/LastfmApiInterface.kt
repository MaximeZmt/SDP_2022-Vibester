package ch.sdp.vibester.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface LastfmApiInterface {
    /**
     * LastfmAPI call with appropriate parameters (based on genre/artist)
     * @return Object: String song list
     */
    @GET("https://ws.audioscrobbler.com/2.0/")
    fun getSongList(@QueryMap paramsMap: MutableMap<String, String>): Call<Any>

    companion object {
        fun createLastfmService(): LastfmApiInterface {
            return ServiceBuilder.buildService(
                "https://ws.audioscrobbler.com/2.0/",
                LastfmApiInterface::class.java
            )
        }
    }
}
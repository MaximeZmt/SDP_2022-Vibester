package ch.sdp.vibester.api

import ch.sdp.vibester.model.Lyric
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface LastfmApiInterface {
    /**
     * LastfmAPI call with appropriate parameters (based on genre/artist)
     * @return Object: String song list
     */
    @GET("https://ws.audioscrobbler.com/2.0/")
    fun getSongList(@QueryMap paramsMap: MutableMap<String, String>): Call<Any>;
}
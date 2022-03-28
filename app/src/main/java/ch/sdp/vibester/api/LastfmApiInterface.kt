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
     * LastfmAPI call with apporpiate parameters (based on genre/artist)
     * @return Object: Strin song list
     */
    @GET("https://ws.audioscrobbler.com/2.0/")
    fun getSongList(@QueryMap paramsMap: MutableMap<String, String>): Call<Object>;

    companion object {
        /**
         * Create a retrofit instance with lastfm API base
         */
        fun create(baseUrl:String = "https://ws.audioscrobbler.com/2.0/"): LastfmApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(LastfmApiInterface::class.java)
        }
    }
}
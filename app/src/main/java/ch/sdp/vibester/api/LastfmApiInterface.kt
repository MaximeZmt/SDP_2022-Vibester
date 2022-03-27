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
     * Given the name of the artist and the title of the track
     * return a Call<Lyric> contains the lyric of the track
     */
    @GET("https://ws.audioscrobbler.com/2.0/")
    fun getSongList(@QueryMap paramsMap: MutableMap<String, String>): Call<Object>;

//    @Query("format") format: String,
//    @Query("method") method: String,
//    @Query("api_key") api_key: String,
//    @Query("tag") tag: String,
//    @Query("artist") artist: String
    companion object {
        /**
         * @param baseUrl base url of the api
         * return an instance of the api interface
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
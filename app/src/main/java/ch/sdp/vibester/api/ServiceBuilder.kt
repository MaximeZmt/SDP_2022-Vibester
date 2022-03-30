package ch.sdp.vibester.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit service that make API calls with chosen API interface.
 */
object ServiceBuilder {
        private fun buildRetrofit(baseUrl:String) =
            Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Connect API interface to a retrofit service to make an API call.
     * @return retrofit service with a connected API interface
     */
    fun <T> buildService(baseUrl: String, service: Class<T>): T {
            return buildRetrofit(baseUrl).create(service)
        }

}
package ch.sdp.vibester.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
        private fun buildRetrofit(baseUrl:String) =
            Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <T> buildService(baseUrl: String, service: Class<T>): T {
            return buildRetrofit(baseUrl).create(service)
        }

}
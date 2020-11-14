package com.example.imagepusher

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class NetworkService  {

    var apiService: API? = null

    fun getService(): API {
        if (apiService == null) {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            apiService = retrofit.create(API::class.java)
        }
        return apiService!!
    }
}
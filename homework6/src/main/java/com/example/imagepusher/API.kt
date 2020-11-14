package com.example.imagepusher

import retrofit2.Call
import retrofit2.http.*

interface API {

    @POST("/3/image")
    fun postImage(
        @Header("Authorization") auth: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("image") file: String
    ): Call<ResponseArray>

    @GET("3/account/{name}")
    fun getUser(
            @Header("Authorization") auth: String,
            @Path("name") name: String
    ): Call<ResponseArray>

}
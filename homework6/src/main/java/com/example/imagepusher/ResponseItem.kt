package com.example.imagepusher

import com.google.gson.annotations.SerializedName

data class ResponseItem (
    @SerializedName("id")
    val id: String,
    @SerializedName("link")
    val link: String
)
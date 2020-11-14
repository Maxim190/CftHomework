package com.example.imagepusher

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseArray (
    @SerializedName("data")
    val data: ResponseItem,
    @SerializedName("success")
    val success: Boolean
)
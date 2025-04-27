package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class LatestProduct(
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double
)

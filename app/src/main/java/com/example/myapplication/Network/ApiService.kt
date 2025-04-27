package com.example.myapplication.Network

import com.example.myapplication.models.LatestProduct
import retrofit2.http.GET

interface ApiService {
    @GET("latest_products.json")
    suspend fun getLatestProducts(): List<LatestProduct>
}
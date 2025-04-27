package com.example.myapplication.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://raw.githubusercontent.com/Sasitha-Ruvin/shoes/main/"

    val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory.create()).build()
    val api: ApiService = retrofit.create(ApiService::class.java)
}
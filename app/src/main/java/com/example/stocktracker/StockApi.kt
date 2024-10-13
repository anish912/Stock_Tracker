package com.example.stocktracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StockApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.alphavantage.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService: StockApiService by lazy {
        retrofit.create(StockApiService::class.java)
    }
}